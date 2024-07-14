package storage.connect.config.cloudflare.aws

import aws.smithy.kotlin.runtime.net.url.Url




class ConfigInfoAWS{
    val bucket: String = ""
    var region: String = ""
    var endpointUrl: Url? = null
    var accessKeyId: String = ""
    var secretAccessKey: String = ""
    var sessionToken: String? = null
    var accountId: String? = null
}

