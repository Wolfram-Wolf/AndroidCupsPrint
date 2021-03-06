package io.github.benoitduffez.cupsprint.app

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import java.security.cert.X509Certificate

import io.github.benoitduffez.cupsprint.HttpConnectionManagement
import io.github.benoitduffez.cupsprint.R

/**
 * Show an untrusted cert info + two buttons to accept or refuse to trust said cert
 */
class UntrustedCertActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.untrusted_cert)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val cert = intent.getSerializableExtra(KEY_CERT) as X509Certificate

        // Build short cert description
        val sb = StringBuilder()
        sb.append("Issuer: ").append(cert.issuerX500Principal.toString())
        sb.append("\nValidity: not before ").append(cert.notBefore.toString())
        sb.append("\nValidity: not after ").append(cert.notAfter.toString())
        sb.append("\nSubject: ").append(cert.subjectX500Principal.name)
        sb.append("\nKey algo: ").append(cert.sigAlgName)

        val certInfo = findViewById<View>(R.id.untrusted_certinfo) as TextView
        certInfo.text = sb

        findViewById<View>(R.id.untrusted_trust_button).setOnClickListener {
            if (HttpConnectionManagement.saveCertificates(this, arrayOf(cert))) {
                Toast.makeText(this@UntrustedCertActivity, R.string.untrusted_trusted, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@UntrustedCertActivity, R.string.untrusted_couldnt_trust, Toast.LENGTH_LONG).show()
            }
            finish()
        }

        findViewById<View>(R.id.untrusted_abort_button).setOnClickListener { finish() }
    }

    companion object {
        val KEY_CERT = "${UntrustedCertActivity::class.java.name}.Certs"
    }
}
