# Coturn

TURN/STUN server for WebRTC (e.g. remote camera relay).

## Options

| Option | Description |
|--------|-------------|
| `username` / `password` | TURN long-term credentials |
| `realm` | TURN realm |
| `listening_ip` | Local IP Coturn binds / relays on |
| `external_ip` | Public IP announced to clients |
| `min_port` / `max_port` | UDP relay port range |
| `use_tls` | Also listen for TURNS on 5349 |
| `cert_file` / `key_file` | PEMs under `/ssl` when `use_tls` is true |

Uses `host_network: true` so UDP relay ports work on the host.

For HTTPS access to Home Assistant, install the **NGINX SSL proxy** add-on from the same repository.
