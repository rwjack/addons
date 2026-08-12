log:
  level: INFO
accessLog: {}

entryPoints:
  web:
    address: ":80"
    http:
      redirections:
        entryPoint:
          to: websecure
          scheme: https
  websecure:
    address: ":443"

certificatesResolvers:
  nestfi:
    acme:
      email: "{{ .options.email }}"
      storage: /data/acme.json
{{- if .options.acme_staging }}
      caServer: "https://acme-staging-v02.api.letsencrypt.org/directory"
{{- end }}
      dnsChallenge:
        provider: rfc2136
        resolvers:
          - "{{ .options.rfc2136_server }}:{{ .options.rfc2136_port }}"

providers:
  file:
    directory: /etc/traefik/dynamic
    watch: true
