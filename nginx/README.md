# NGINX SSL proxy

Home Assistant Supervisor add-on: TLS reverse proxy.

- `domains`: FQDNs that terminate TLS (supports multiple hostnames, e.g. remote + local)
- Certificates from `/ssl` (Let’s Encrypt add-on)
- Proxies to Home Assistant (including `/.well-known` for app association files)

TURN/STUN is a **separate** add-on (`coturn`) in this same repository.

Add this repository in Supervisor → Add-on store → Repositories:

`https://github.com/rwjack/addons`
