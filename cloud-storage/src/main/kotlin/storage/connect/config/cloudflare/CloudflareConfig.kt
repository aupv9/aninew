package storage.connect.config.cloudflare

import storage.connect.config.cloudflare.aws.ConfigInfoAWS

interface CloudflareConfig {
    fun createConfigInfoForClient(mapInfo: Map<String, Any>): ConfigInfoAWS?

}