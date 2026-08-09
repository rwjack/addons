# NGINX SSL proxy

TLS reverse proxy for Home Assistant.

## Options

| Option | Description |
|--------|-------------|
| `domains` | Hostnames that terminate TLS (one or more FQDNs) |
| `certfile` / `keyfile` | PEM files under `/ssl` |
| `hsts` | Strict-Transport-Security value (empty string to disable) |

## Home Assistant HTTP

Requires:

```yaml
http:
  use_x_forwarded_for: true
  trusted_proxies:
    - 172.30.33.0/24
```

For WebRTC TURN/STUN, install the **Coturn** add-on from the same repository.
