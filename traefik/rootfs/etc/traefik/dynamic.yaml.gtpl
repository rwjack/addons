{{- if .variables.turn_sni }}
tcp:
  routers:
    turn:
      entryPoints:
        - websecure
      rule: "HostSNI(`{{ .variables.turn_sni }}`)"
      service: coturn
      tls:
        passthrough: true
  services:
    coturn:
      loadBalancer:
        servers:
          - address: "{{ .variables.turn_upstream }}:{{ .options.turn_upstream_port }}"
{{- end }}
http:
  routers:
    ha:
      entryPoints:
        - websecure
      rule: "{{ .variables.host_rule }}"
      service: homeassistant
{{- if .options.hsts }}
      middlewares:
        - hsts
{{- end }}
      tls:
        certResolver: nestfi
        domains:
          - main: "{{ .variables.cert_main }}"
            sans:
{{- range .variables.cert_sans }}
              - "{{ . }}"
{{- end }}
  services:
    homeassistant:
      loadBalancer:
        servers:
          - url: "http://homeassistant.local.hass.io:{{ .variables.port }}"
        passHostHeader: true
{{- if .options.hsts }}
  middlewares:
    hsts:
      headers:
        stsSeconds: 31536000
        stsIncludeSubdomains: true
{{- end }}
