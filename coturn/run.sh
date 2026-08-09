#!/usr/bin/env bash
set -euo pipefail

OPTIONS_FILE="/data/options.json"

USERNAME="$(jq -r '.username' "$OPTIONS_FILE")"
PASSWORD="$(jq -r '.password' "$OPTIONS_FILE")"
REALM="$(jq -r '.realm' "$OPTIONS_FILE")"
LISTENING_IP="$(jq -r '.listening_ip' "$OPTIONS_FILE")"
EXTERNAL_IP="$(jq -r '.external_ip' "$OPTIONS_FILE")"
MIN_PORT="$(jq -r '.min_port' "$OPTIONS_FILE")"
MAX_PORT="$(jq -r '.max_port' "$OPTIONS_FILE")"
USE_TLS="$(jq -r '.use_tls' "$OPTIONS_FILE")"
CERT_FILE="$(jq -r '.cert_file' "$OPTIONS_FILE")"
KEY_FILE="$(jq -r '.key_file' "$OPTIONS_FILE")"

ARGS=(
  --no-cli
  --log-file=stdout
  --fingerprint
  --lt-cred-mech
  --realm="$REALM"
  --user="$USERNAME:$PASSWORD"
  --listening-ip="$LISTENING_IP"
  --relay-ip="$LISTENING_IP"
  --listening-port=3478
  --min-port="$MIN_PORT"
  --max-port="$MAX_PORT"
  --no-multicast-peers
  --stale-nonce
)

if [[ -n "$EXTERNAL_IP" && -n "$LISTENING_IP" && "$EXTERNAL_IP" != "null" && "$LISTENING_IP" != "null" ]]; then
  ARGS+=(--external-ip="$EXTERNAL_IP/$LISTENING_IP")
fi

if [[ "$USE_TLS" == "true" ]]; then
  ARGS+=(
    --tls-listening-port=5349
    --cert="/ssl/$CERT_FILE"
    --pkey="/ssl/$KEY_FILE"
  )
fi

exec turnserver "${ARGS[@]}"
