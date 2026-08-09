daemon off;
error_log stderr;
pid /var/run/nginx.pid;

events { worker_connections 1024; }

http {
  map_hash_bucket_size 128;
  map $http_upgrade $connection_upgrade {
    default upgrade;
    '' close;
  }
  map $http_host $forward_host {
    default $http_host;
    '' $host;
  }
  server_tokens off;
  server_names_hash_bucket_size 128;
  ssl_protocols TLSv1.2 TLSv1.3;
  ssl_prefer_server_ciphers off;

  server {
    server_name _;
    listen 80 default_server;
    listen 443 ssl default_server;
    listen [::]:80 default_server;
    listen [::]:443 ssl default_server;
    http2 on;
    ssl_reject_handshake on;
    return 444;
  }

  server {
    server_name{{ range .options.domains }} {{ . }}{{ end }};
    listen 80;
    listen [::]:80;
    return 301 https://$host$request_uri;
  }

  server {
    server_name{{ range .options.domains }} {{ . }}{{ end }};
    listen 443 ssl;
    listen [::]:443 ssl;
    http2 on;
    ssl_certificate /ssl/{{ .options.certfile }};
    ssl_certificate_key /ssl/{{ .options.keyfile }};
    ssl_session_timeout 1d;
    ssl_session_cache shared:MozSSL:10m;
    ssl_session_tickets off;
{{- if .options.hsts }}
    add_header Strict-Transport-Security "{{ .options.hsts }}" always;
{{- end }}
    proxy_buffering off;
    client_max_body_size 64m;

    # Proxy app traffic and platform well-known (AASA / assetlinks) to Home Assistant.
    location / {
      proxy_pass http://homeassistant.local.hass.io:{{ .variables.port }};
      proxy_set_header Origin $http_origin;
      proxy_set_header X-Forwarded-Proto $scheme;
      proxy_set_header Host $forward_host;
      proxy_redirect http:// https://;
      proxy_http_version 1.1;
      proxy_set_header Upgrade $http_upgrade;
      proxy_set_header Connection $connection_upgrade;
      proxy_set_header X-Forwarded-Host $forward_host;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
  }
}
