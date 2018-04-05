/*
 * This file is part of Gradoop.
 *
 * Gradoop is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Gradoop is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Gradoop.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gradoop.demo.server;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Basic class, used for starting and stopping the server.
 */
public class Server {

  /**
   * URI that specifies where the server is run.
   */
//  private static final URI BASE_URI = getBaseURI();
  /**
   * Default port
   */
  private static final int PORT = 2342;
  /**
   * Path to demo application
   */
  private static final String APPLICATION_PATH = "gradoop/html/grouping.html";

  /**
   * Creates the base URI.
   * @return Base URI
   */
  private static URI getBaseURI(String ip) {
    return UriBuilder.fromUri("http://" + ip + "/").port(PORT).build();
  }

  /**
   * Starts the server and adds the request handlers.
   *
   * @return the running server
   * @throws IOException if server creation fails
   */
  private static HttpServer startServer(String[] args) throws IOException {
    System.out.println("Starting grizzly...");
    ResourceConfig rc = new PackagesResourceConfig("org/gradoop/demo/server");
    rc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
    String ip = args.length == 0 ? "localhost" : args[0];
    URI baseURI = getBaseURI(ip);
    HttpServer server = GrizzlyServerFactory.createHttpServer(baseURI, rc);
    HttpHandler staticHandler = new StaticHttpHandler(
      Server.class.getResource("/web").getPath());
    server.getServerConfiguration().addHttpHandler( staticHandler, "/gradoop" );
    System.out.printf("org.gradoop.demos.grouping.server started at %s%s%n" +
        "Kill the process or ^c to stop it.%n", baseURI, APPLICATION_PATH);
    return server;
  }

  /**
   * Main method. Run this to start the server.
   *
   * @param args command line parameters
   * @throws IOException if server creation fails
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    HttpServer httpServer = startServer(args);
//    System.in.read();
//    httpServer.stop();
      // for nohup
      BlockingQueue<String> q = new ArrayBlockingQueue<>(1);
      synchronized (q) {
          while (q.isEmpty())
              q.wait();
      }
  }
}
