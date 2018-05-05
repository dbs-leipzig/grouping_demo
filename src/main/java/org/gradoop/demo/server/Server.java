/*
 * Copyright © 2014 - 2018 Leipzig University (Database Research Group)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradoop.demo.server;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.RemoteEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.ws.rs.core.UriBuilder;

import static java.lang.System.exit;
import static java.lang.System.setOut;
import static java.util.Objects.requireNonNull;
import static org.apache.flink.api.java.ExecutionEnvironment.createLocalEnvironment;
import static org.apache.flink.api.java.ExecutionEnvironment.createRemoteEnvironment;
import static org.gradoop.demo.server.LocalGradoopGraphsetStore.DEFAULT_LOCAL_PATH;

/**
 * Basic class, used for starting and stopping the server.
 */
public class Server {

    /**
     * URI that specifies where the server is run.
     */
//  private static final URI BASE_URI = getBaseURI();
    public static final String DEFAULT_JM = "local";
    static volatile ExecutionEnvironment ENV = null; // volatile is just to ensure visibility
    static volatile GradoopGraphsetStore LOCAL_STORE = null;
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
     *
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
    private static HttpServer startServer(String[] args) throws IOException, URISyntaxException {
        System.out.println("Starting grizzly...");
        ResourceConfig rc = new PackagesResourceConfig("org/gradoop/demo/server");
        rc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        ParameterTool params = ParameterTool.fromArgs(args);
        String ip = params.has("ip") ? params.get("ip") : "localhost";
        String jmHost = params.has("jmhost") ? params.get("jmhost") : DEFAULT_JM;
        int jmPort = -1;
        if (!DEFAULT_JM.equals(jmHost) && !params.has("jmport")) {
            System.out.println("Error: provide Job Manager Port; exiting");
            exit(1);
        }
        if (!DEFAULT_JM.equals(jmHost)) {
            jmPort = params.getInt("jmport");
        }
        URI baseURI = getBaseURI(ip);
        ENV = getExecutionEnvironment(jmHost, jmPort);
        String hdfsParam = "hdfs";
        if (params.has(hdfsParam)) {
            LOCAL_STORE = new LocalGradoopGraphsetStore(new URI(params.get(hdfsParam)), DEFAULT_LOCAL_PATH);
        } else {
            LOCAL_STORE = new LocalGradoopGraphsetStore(null, DEFAULT_LOCAL_PATH);
        }
        System.out.println("Execution Environment: " + ENV);
        System.out.println(LOCAL_STORE);
        HttpServer server = GrizzlyServerFactory.createHttpServer(baseURI, rc);
        HttpHandler staticHandler = new StaticHttpHandler(
            Server.class.getResource("/web").getPath());
        server.getServerConfiguration().addHttpHandler(staticHandler, "/gradoop");
        System.out.printf("org.gradoop.demos.grouping.server started at %s%s%n" +
            "Kill the process or ^c to stop it.%n", baseURI, APPLICATION_PATH);
        return server;
    }

    private static ExecutionEnvironment getExecutionEnvironment(String jmHost, int jmPort) throws MalformedURLException {
        requireNonNull(jmHost);
        if (DEFAULT_JM.equals(jmHost)) {
            return createLocalEnvironment();
        }
//        String m2 = "/.m2/repository/org/gradoop";
//        String[] jarFiles = {
//          System.getProperty("user.home") + m2 + "/gradoop-common/0.3.2/gradoop-common-0.3.2.jar",
//          System.getProperty("user.home") + m2 + "/gradoop-flink/0.3.2/gradoop-flink-0.3.2.jar"
//        };
        String[] jarFiles = {
            System.getProperty("user.dir") + "/target/gradoop-demo-shaded.jar"
        };
//        URL[] globalCP = {
//            new URL("file://" + System.getProperty("user.dir") + "/target/gradoop-demo-shaded.jar")
//        };
        ExecutionEnvironment ee =  ExecutionEnvironment.createRemoteEnvironment(jmHost, jmPort, null);
//        ExecutionEnvironment ee =  ExecutionEnvironment.createRemoteEnvironment(jmHost, jmPort);

        System.out.println("ee exec mode: " + ee.getConfig().getExecutionMode());
        return ee;
    }

    /**
     * Main method. Run this to start the server.
     *
     * @param args command line parameters
     * @throws IOException if server creation fails
     */
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        HttpServer httpServer = startServer(args);
//    System.in.read();
//    httpServer.stop();
        // for nohup
        BlockingQueue<String> q = new ArrayBlockingQueue<>(1);
        synchronized (q) { // sync on local variable on for an indefinite wait
            while (q.isEmpty()) {
                q.wait();
            }
        }
        httpServer.stop();
    }
}
