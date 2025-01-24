package ch.ivyteam.testendpoint;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
