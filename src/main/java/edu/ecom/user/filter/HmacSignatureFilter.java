package edu.ecom.user.filter;

import edu.ecom.user.service.HmacSigningService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class HmacSignatureFilter extends OncePerRequestFilter {
  private final HmacSigningService signingService;

  @Autowired
  public HmacSignatureFilter(HmacSigningService signingService) {
    this.signingService = signingService;
  }

  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String signature = request.getHeader("X-Auth-Signature");
    String timestamp = request.getHeader("X-Auth-Timestamp");
    String serviceId = request.getHeader("X-Service-ID");

    if("gateway-service".equals(serviceId)) {
      // Skip HMAC verification for the gateway service
      chain.doFilter(request, response);
      return;
    }
    if (signature == null || timestamp == null || serviceId == null) {
      response.sendError(401, "Missing required headers");
      return;
    }
    // 1. Validate timestamp freshness (±30 seconds)
    if (Math.abs(Instant.now().getEpochSecond() - Long.parseLong(timestamp)) > 30) {
      throw new SecurityException("Expired request");
    }

    // 2. Verify HMAC signature
    if (!signingService.verifySignature(serviceId, timestamp, request.getRequestURI(), request.getMethod(), signature)) {
      response.sendError(401, "Invalid signature");
      return;
    }

    chain.doFilter(request, response);
  }
}