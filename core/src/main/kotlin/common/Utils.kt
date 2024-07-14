package common

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import common.Utils.Companion.generateKeyPair
import common.Utils.Companion.getPublicKeyFromString
import common.Utils.Companion.hmacWithJava
import common.Utils.Companion.keyPairToPem
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemWriter
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun main() {


    println(hmacWithJava("HmacSHA256","baeldung","123456"))
    println(hmacWithJava("HmacSHA256","baeldung","123456"))
    // Your claims
//    val claims = mapOf(
//        "sub" to "1234567890",
//        "name" to "John Doe",
//        "iat" to System.currentTimeMillis() / 1000
//    )
//
//    val keypair = generateKeyPair()
//    val encodePublicKey = Base64.getEncoder().encodeToString(keypair.private.encoded)
//
//    println(getPublicKeyFromString(encodePublicKey))

    // Generate the JWT
//    val token = generateToken(claims, keypair.private)
//    println("Generated Token: $token")

//    val algorithm  = Algorithm.RSA256(keypair.public as RSAPublicKey?, null)
//
//     Verify the JWT
//    val verifiedToken = verifyToken(algorithm, token)
//    println("Verified Token: $verifiedToken")
// Create a KeyPair (replace with your own key generation logic)

}


class Utils private constructor(){


    companion object {

        fun hmacWithJava( algorithm: String ,  data: String,  key: String): String{
            val secretKeySpec = SecretKeySpec(key.toByteArray(), algorithm);
            val mac = Mac.getInstance(algorithm);
            mac.init(secretKeySpec);
            return mac.doFinal(data.toByteArray()).joinToString(""){
                String.format("%02x", it)
            }
        }


        const val USER_SESSION = "user_session"
        const val LOGGED_IN = "logged_in"
        const val AG_SESSION = "_ag_sess"
        const val FIRST_LOGGED = "f_logged"

        const val SCHEMA = "http://"

        data class RSAKeyPair(val privateKey: String, val publicKey: String)

        fun keyPairToPem(keyPair: KeyPair): String {
            Security.addProvider(org.bouncycastle.jce.provider.BouncyCastleProvider())

            val privatePem = privateKeyToPem(keyPair.private)
            val publicPem = publicKeyToPem(keyPair.public)

            return privatePem + publicPem
        }

         fun privateKeyToPem(privateKey: PrivateKey): String {
            val stringWriter = StringWriter()
            PemWriter(stringWriter).use { pemWriter ->
                pemWriter.writeObject(PemObject("PRIVATE KEY", privateKey.encoded))
            }
            return stringWriter.toString()
        }

         fun publicKeyToPem(publicKey: PublicKey): String {
            val stringWriter = StringWriter()
            PemWriter(stringWriter).use { pemWriter ->
                pemWriter.writeObject(PemObject("PUBLIC KEY", publicKey.encoded))
            }
            return stringWriter.toString()
        }

        fun verifyToken(algorithm: Algorithm, token: String): Boolean {
            return try {
                JWT.require(algorithm)
                    .build()
                    .verify(token)
                true
            } catch (e: Exception) {
                false
            }
        }

         fun parseToken(token: String): Map<String, Any> {
             val decodedToken = JWT.decode(token)
             val claims = decodedToken.claims
             return claims.mapValues { it.value.asString() }
         }

        fun getExpiryTime(token: String): Date {
            val decodedToken = JWT.decode(token)
            return decodedToken.expiresAt
        }


        fun isTokenExpired(tokenDate: Date,now: Date): Boolean =  tokenDate.time - now.time > 0

        fun generateId(): String =
             UUID.randomUUID().toString()


        fun createKeyPair(publicKeyString: String, privateKeyString: String): KeyPair {
            val publicKeyBytes = Base64.getDecoder().decode(publicKeyString)
            val privateKeyBytes = Base64.getDecoder().decode(privateKeyString)

            val publicKeySpec = X509EncodedKeySpec(publicKeyBytes)
            val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)

            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKey = keyFactory.generatePublic(publicKeySpec)
            val privateKey = keyFactory.generatePrivate(privateKeySpec)

            return KeyPair(publicKey, privateKey)
        }

        fun generateKeyPair(): KeyPair {
            // Choose the algorithm (e.g., RSA)
            val algorithm = "RSA"

            // Generate a key pair generator for the specified algorithm
            val keyPairGenerator = KeyPairGenerator.getInstance(algorithm)

            // Initialize the key pair generator with the desired key size (e.g., 2048 bits)
            keyPairGenerator.initialize(2048)

            // Generate the key pair
            return keyPairGenerator.genKeyPair()
        }

         fun getPublicKey(keypair: KeyPair): String {
            return Base64.getEncoder().encodeToString(keypair.public.encoded)
        }

        fun getPublicKeyFromString(key: String): PublicKey {
            // Decode the Base64-encoded key bytes
            val keyBytes = Base64.getDecoder().decode(key)

            // Create a key specification from the decoded key bytes
            val keySpec = X509EncodedKeySpec(keyBytes)

            // Get the RSA key factory
            val keyFactory = KeyFactory.getInstance("RSA")

            // Generate the public key from the key specification
            return keyFactory.generatePublic(keySpec)
        }

         fun getPrivateKey(privateKeyPEM: String): PrivateKey {
            val keyBytes = Base64.getDecoder().decode(privateKeyPEM)
            val keySpec = PKCS8EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            return keyFactory.generatePrivate(keySpec)
        }
        fun generateTokenWithUUID(): String = UUID.randomUUID().toString()



        fun generateToken(claims: Map<String, Any>, expiryDate: Date, privateKey: PrivateKey): String {
            val algorithm: Algorithm = Algorithm.RSA256(null, privateKey as RSAPrivateKey?)
            val jwtBuilder = JWT.create().withIssuedAt(Date()).withExpiresAt(expiryDate)
            claims.forEach { (key, value) ->
                when (value) {
                    is String -> jwtBuilder.withClaim(key, value)
                    is Int -> jwtBuilder.withClaim(key, value)
                    is Long -> jwtBuilder.withClaim(key, value)
                    is Boolean -> jwtBuilder.withClaim(key, value)
                    is Date -> jwtBuilder.withClaim(key, value)
                    else -> throw IllegalArgumentException("Unsupported claim type: ${value::class.java.name}")
                }
            }
            return jwtBuilder.sign(algorithm)
        }


    }
}