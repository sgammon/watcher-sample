
package hello;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public final class App {
  // Change this value to test bazel-watcher's reload
  private static final String message = "Hello world!";

  // Endpoint where we are expecting to find the watcher JS.
  private static final String js = "http://localhost:35729/livereload.js";

  // Page wrap (`$content` is replaced with page content).
  private static final String page = "<html><head><title>Test</title></head><body>$content</body></html>";

  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext("/test", new TestHandler());
    server.setExecutor(null);
    System.out.println("Live reload is " +
      ("1".equals(System.getProperty("LIVE", "0")) ? "ACTIVE" : "INACTIVE") + ".");
    System.out.println("Handler listening at 0.0.0.0:8000/test");
    server.start();
  }

  static class TestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      final byte[] responseData;
      exchange.getResponseHeaders().add("Content-Type", "text/html;charset=UTF-8");

      if ("1".equals(System.getProperty("LIVE", "0"))) {
        // with live-reload active, include a reference to the script
        responseData = page.replace("$content",
          (("<b>" + message + "</b>") +
          ("<script type=\"text/javascript\" src=\"" + js + "\"></script>"))).getBytes();
      } else {
        responseData = page.replace("$content", "<b>" + message + "</b>").getBytes();
      }

      exchange.sendResponseHeaders(200, responseData.length);
      OutputStream out = exchange.getResponseBody();
      out.write(responseData);
      out.close();
    }
  }
}

