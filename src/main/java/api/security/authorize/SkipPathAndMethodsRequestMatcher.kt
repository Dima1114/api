package api.security.authorize

import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

import javax.servlet.http.HttpServletRequest
import java.util.stream.Collectors

class SkipPathAndMethodsRequestMatcher(pathsToSkip: List<String>,
                                       private val methodsToSkip: List<String>, processingPath: String) : RequestMatcher {
    private val matchers: OrRequestMatcher
    private val processingMatcher: RequestMatcher

    init {

        val m = pathsToSkip.map { AntPathRequestMatcher(it) }

        this.matchers = OrRequestMatcher(m)
        this.processingMatcher = AntPathRequestMatcher(processingPath)
    }

    override fun matches(request: HttpServletRequest): Boolean {

        if (matchers.matches(request)) {
            return false
        }

        return if (methodsToSkip.contains(request.method)) false else processingMatcher.matches(request)
    }
}