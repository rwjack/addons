# Coturn

Home Assistant Supervisor add-on: TURN/STUN for WebRTC.

Configure listening/external IPs and credentials for remote camera relay.
TLS reverse proxy is a **separate** add-on (`traefik`) in this same repository, which
also SNI-passthroughs TURNS-over-443 to this add-on.

Add this repository in Supervisor → Add-on store → Repositories:

`https://github.com/rwjack/addons`
