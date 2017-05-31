package ru.studmanager;

class Main {
    public static void main(String[] args) throws Exception {
        JettyLauncher.server(new JettyLauncher.JettyConfig("localhost", 8080, 8081, 1000, 1000,
                new JettyLauncher.ThreadPool(3, 15, 1000))).withDefaultWebApp(Main.class.getClassLoader(), "/").run();
    }
}
