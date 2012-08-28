package com.rockontrol.yaogan.spring.ext;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;

public class CasAuthenticationEntryPoint implements AuthenticationEntryPoint,
      InitializingBean {
   private ServiceProperties serviceProperties;

   private String loginUrl;

   private boolean encodeServiceUrlWithSessionId = true;

   public void afterPropertiesSet() throws Exception {
      Assert.hasLength(this.loginUrl, "loginUrl must be specified");
      Assert.notNull(this.serviceProperties, "serviceProperties must be specified");
   }

   public final void commence(final HttpServletRequest servletRequest,
         final HttpServletResponse response,
         final AuthenticationException authenticationException) throws IOException,
         ServletException {

      final String urlEncodedService = createServiceUrl(servletRequest, response);
      final String redirectUrl = createRedirectUrl(urlEncodedService);

      preCommence(servletRequest, response);

      response.sendRedirect(redirectUrl);
   }

   protected String createServiceUrl(final HttpServletRequest request,
         final HttpServletResponse response) {
      return CommonUtils.constructServiceUrl(null, response,
            this.serviceProperties.getService(), null,
            this.serviceProperties.getArtifactParameter(),
            this.encodeServiceUrlWithSessionId);
   }

   protected String createRedirectUrl(final String serviceUrl) {
      return CommonUtils.constructRedirectUrl(this.loginUrl,
            this.serviceProperties.getServiceParameter(), serviceUrl,
            this.serviceProperties.isSendRenew(), false);
   }

   protected void preCommence(final HttpServletRequest request,
         final HttpServletResponse response) {

   }

   public final String getLoginUrl() {
      return this.loginUrl;
   }

   public final ServiceProperties getServiceProperties() {
      return this.serviceProperties;
   }

   public final void setLoginUrl(final String loginUrl) {
      this.loginUrl = loginUrl;
   }

   public final void setServiceProperties(final ServiceProperties serviceProperties) {
      this.serviceProperties = serviceProperties;
   }

   @Deprecated
   public final void setEncodeServiceUrlWithSessionId(
         final boolean encodeServiceUrlWithSessionId) {
      this.encodeServiceUrlWithSessionId = encodeServiceUrlWithSessionId;
   }

   @Deprecated
   protected boolean getEncodeServiceUrlWithSessionId() {
      return this.encodeServiceUrlWithSessionId;
   }
}
