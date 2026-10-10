## Requirements

-   Java JDK
-   IntelliJ IDEA
-   Apache Tomcat configured in IntelliJ

## How to Run

1.  Open the project in IntelliJ IDEA.

2.  Configure and start the Tomcat run configuration for the project.

3.  Open the welcome servlet in your browser:

    `http://localhost:8080/demo_war_exploded`

4.  Select **1** or **2** and submit the form. The controller forwards
    you to `page1.html` or `page2.html`.

5.  To test the desktop client, leave Tomcat running and run
    `DesktopClient.main()`. The client sends a POST request to
    `DesktopServlet` and prints the plain-text response in its console.

> If your Tomcat deployment uses a different application context,
> replace `demo_war_exploded` in the URL with your configured context
> path.

## Files

  -----------------------------------------------------------------------
File                                Purpose
  ----------------------------------- -----------------------------------
`WelcomeServlet.java`               Dynamically generates the welcome
HTML page and a form with radio
buttons for values `1` and `2`. The
form submits its selection to
`ControllerServlet`.

`ControllerServlet.java`            Reads the submitted `value`, logs
request details (HTTP method,
client IP, User-Agent, accepted
languages, and parameter), and
forwards the request to
`page1.html`, `page2.html`, or
`eroare.html` for an
invalid/missing selection.

`DesktopServlet.java`               Handles requests from the desktop
client. It reads the `value`
parameter and returns that value as
plain text instead of forwarding to
an HTML page.

`DesktopClient.java`                A standalone Java client with a
`main()` method. It sends an HTTP
POST request to `DesktopServlet`
and prints the server's response.

`HelloServlet.java`                 The default/example servlet created
in the original project. It may be
used for testing and is not
required for the main assignment
flow.

`page1.html`                        Static HTML page displayed when the
user selects `1` in the welcome
form.

`page2.html`                        Static HTML page displayed when the
user selects `2` in the welcome
form.

`eroare.html`                       Error page displayed by the
controller if the selection is
missing or invalid, if this page is
included in the project.

`WEB-INF/web.xml`                   Web application deployment
configuration for the application.
  -----------------------------------------------------------------------

## Request Flow

### Browser

1.  The browser requests `/welcome`.
2.  `WelcomeServlet` generates the form.
3.  The form submits the selected value to `ControllerServlet`.
4.  `ControllerServlet` logs the request information and forwards to the
    corresponding HTML page.

### Desktop client

1.  Start Tomcat.
2.  Run `DesktopClient.main()`.
3.  The client sends `POST /desktop` with a form parameter such as
    `value=1`.
4.  `DesktopServlet` responds with plain text, such as `1`.

The desktop client must use the same host, port, and application context
as the running Tomcat deployment.