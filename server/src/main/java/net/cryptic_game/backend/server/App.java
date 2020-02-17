package net.cryptic_game.backend.server;

import net.cryptic_game.backend.base.AppBootstrap;
import net.cryptic_game.backend.base.api.ApiHandler;
import net.cryptic_game.backend.server.api.ServerApiEndpointExecutor;
import net.cryptic_game.backend.server.config.ServerConfig;
import net.cryptic_game.backend.server.deamon.DaemonHandler;
import net.cryptic_game.backend.server.server.NettyServerHandler;
import net.cryptic_game.backend.server.server.http.HttpCodec;
import net.cryptic_game.backend.server.server.websocket.WebSocketCodec;
import net.cryptic_game.backend.server.server.websocket.endpoints.InfoEndpoints;
import net.cryptic_game.backend.server.server.websocket.endpoints.UserEndpoints;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App extends AppBootstrap {

    private DaemonHandler daemonHandler;
    private NettyServerHandler serverHandler;

    public App() {
        super(ServerConfig.CONFIG, "Java-Server");
    }

    public static void main(String[] args) {
        new App();
    }

    @Override
    protected void init() {
        try {
            throw new IOException("Messagedqw dqw");
        } catch (IOException e) {
            LoggerFactory.getLogger(App.class).error("Error while executing some stuff.", e);
        }

        this.daemonHandler = new DaemonHandler();
        this.serverHandler = new NettyServerHandler();

        this.serverHandler.addServer("daemon",
                this.config.getAsString(ServerConfig.DAEMON_HOST),
                this.config.getAsInt(ServerConfig.DAEMON_PORT),
                new HttpCodec());

        this.serverHandler.addServer("websocket",
                this.config.getAsString(ServerConfig.WEBSOCKET_HOST),
                this.config.getAsInt(ServerConfig.WEBSOCKET_PORT),
                new WebSocketCodec());

        this.serverHandler.addServer("http",
                this.config.getAsString(ServerConfig.HTTP_HOST),
                this.config.getAsInt(ServerConfig.HTTP_PORT),
                new HttpCodec());

    }

    @Override
    protected void start() {
        this.serverHandler.start();
    }

    @Override
    protected void initApi() {
        this.apiHandler = new ApiHandler(ServerApiEndpointExecutor.class);
        apiHandler.registerApiCollection(new UserEndpoints());
        apiHandler.registerApiCollection(new InfoEndpoints());
    }
}
