# Coturn

TURN/STUN server for WebRTC (e.g. remote camera relay).

## Options

| Option | Description |
|--------|-------------|
| `username` / `password` | TURN long-term credentials |
| `realm` | TURN realm (fallback when `turn_domain` is unset) |
| `listening_ip` | Local IP Coturn binds / relays on |
| `external_ip` | Public IP announced to clients |
| `min_port` / `max_port` | UDP relay port range |
| `use_tls` | Also listen for TURNS on `tls_listening_port` |
| `tls_listening_port` | Direct TURNS port (default `5349`) |
| `turn_domain` | The `t-<core>.ui.nestfi.rs` SAN; used as realm when set |
| `cert_file` / `key_file` | PEMs under `/ssl` when `use_tls` is true |

Uses `host_network: true` so UDP relay ports work on the host.

`boot: manual` by default — the integration turns this add-on on/off (start/stop,
`boot=auto`/`manual`) based on the WebRTC panel's "Enable TURN relay" toggle. It is not
meant to be started by hand in normal operation.

## TURNS on 443

Coturn itself only listens on 3478 and `tls_listening_port` (5349). TURNS-over-443 is
served by the **Traefik** add-on, which SNI-passthroughs connections for the `t-<core>`
hostname straight to this add-on's `tls_listening_port` — coturn does not bind host port
443 directly. See the Traefik add-on's docs and `docs/nestfi-access-plan.md`.

## Certificates

`use_tls` requires `/ssl/<cert_file>` and `/ssl/<key_file>` to exist and cover the
`turn_domain` SAN. These are written by the Traefik add-on's `certdump` service, not by
coturn. If they are missing when `use_tls` is set, this add-on logs a warning and starts
without TLS rather than crash-looping — it will pick up TLS on its next restart once the
cert appears (`certdump` also triggers that restart automatically).

For HTTPS access to Home Assistant, install the **Traefik** add-on from the same repository.
