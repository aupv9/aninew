package autogear.frontapi.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException


class IOAuthenticateFilter: GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        this.doFilterInternal( request as HttpServletRequest, response as HttpServletResponse, chain)
    }

    private fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain){
        try {
            chain.doFilter(request, response)
        }catch (_: IOException){

        }
    }

}