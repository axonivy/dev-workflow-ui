package ch.ivyteam.testendpoint;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("iframe")
@PermitAll
public class IframeEndpoint {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("noiframe")
  public Response noiframe() {
    return Response.status(200)
        .header("x-frame-options", "DENY")
        .build();
  }

}
