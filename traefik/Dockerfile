ARG BUILD_FROM
FROM $BUILD_FROM

# nestfi-access-plan.md §3.1: pinned to a specific Traefik release, not "latest" —
# this binary terminates TLS for the whole home and the config schema in this
# add-on is v3 syntax that does not carry back to v2. Bump deliberately.
ARG TRAEFIK_VERSION=v3.7.10
ARG BUILD_ARCH

RUN apk add --no-cache jq inotify-tools curl \
 && case "${BUILD_ARCH}" in \
      aarch64) TARCH=linux_arm64 ;; \
      amd64)   TARCH=linux_amd64 ;; \
      *) echo "unsupported arch ${BUILD_ARCH}" >&2; exit 1 ;; \
    esac \
 && curl -fsSL -o /tmp/traefik.tar.gz \
      "https://github.com/traefik/traefik/releases/download/${TRAEFIK_VERSION}/traefik_${TRAEFIK_VERSION}_${TARCH}.tar.gz" \
 && curl -fsSL -o /tmp/traefik_checksums.txt \
      "https://github.com/traefik/traefik/releases/download/${TRAEFIK_VERSION}/traefik_${TRAEFIK_VERSION}_checksums.txt" \
 && grep " traefik_${TRAEFIK_VERSION}_${TARCH}.tar.gz\$" /tmp/traefik_checksums.txt \
      | sed "s|traefik_${TRAEFIK_VERSION}_${TARCH}.tar.gz|/tmp/traefik.tar.gz|" \
      | sha256sum -c - \
 && tar -xzf /tmp/traefik.tar.gz -C /usr/local/bin traefik \
 && chmod +x /usr/local/bin/traefik \
 && rm -f /tmp/traefik.tar.gz /tmp/traefik_checksums.txt \
 && traefik version

COPY rootfs /
