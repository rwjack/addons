# Traefik SSL proxy

TLS reverse proxy for Home Assistant, replacing the old `nginx` add-on and the
`core_letsencrypt` add-on — ACME (DNS-01 via rfc2136) now lives inside Traefik itself.
Also SNI-passthroughs TURNS-over-443 straight to the **Coturn** add-on, so a single host
port 443 serves both HTTPS and TURN.

See `docs/nestfi-access-plan.md` for the full design and rollout.

## Options

| Option | Description |
|--------|-------------|
| `domains` | Hostnames that terminate TLS for Home Assistant (`e-`/`i-`) |
| `turn_domain` | The `t-<core>` SAN. Always on the cert once provisioned, regardless of `turn_domains` |
| `turn_domains` | SNI names routed to Coturn on 443. Empty ⇒ no TCP router (safe default) |
| `turn_upstream` / `turn_upstream_port` | Coturn's `listening_ip` / `tls_listening_port` |
| `email` / `rfc2136_*` | ACME account + DNS-01 rfc2136 credentials |
| `certfile` / `keyfile` | PEM filenames `certdump` writes under `/ssl` |
| `hsts` | Strict-Transport-Security value (empty disables it) |
| `coturn_slug` | Add-on slug `certdump` restarts on cert renewal |
| `acme_staging` | Use the LE staging directory — use this while iterating |

## How TLS routing works

- Port 80 redirects to 443.
- Port 443 is a single Traefik entrypoint. An SNI matching `turn_domains` is passed
  through **encrypted, unterminated** straight to Coturn's TLS listener. Everything else
  is routed as normal HTTPS to Home Assistant.
- The certificate always covers `domains` **and** `turn_domain` (when set) — the cert is
  provisioned once and is independent of whether TURNS routing is currently enabled, so
  flipping the Coturn toggle never triggers a reissue.
- Unlike the old nginx add-on's `ssl_reject_handshake`, an SNI that matches neither list
  gets Traefik's default self-signed cert and a 404 rather than a refused handshake —
  cosmetic difference, not a routing gap.

## Certificates: `acme.json` → PEM

Traefik keeps certificates in `/data/acme.json`. A second internal service, `certdump`,
watches that file and writes plain PEM files under `/ssl` — because Coturn needs
`--cert`/`--pkey` paths and the integration's cert-status reader expects
`/ssl/fullchain.pem`. `certdump` also restarts the Coturn add-on (via `coturn_slug`)
the moment a new certificate lands, so a renewal never leaves Coturn serving a stale one.

## Home Assistant HTTP

Requires:

```yaml
http:
  use_x_forwarded_for: true
  trusted_proxies:
    - 172.30.33.0/24
```

For WebRTC TURN/STUN, install the **Coturn** add-on from the same repository.
