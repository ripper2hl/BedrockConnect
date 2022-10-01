package com.pyratron.pugmatt.bedrockconnect;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.nukkitx.protocol.bedrock.*;
import com.pyratron.pugmatt.bedrockconnect.listeners.PacketHandler;
import com.pyratron.pugmatt.bedrockconnect.utils.BedrockProtocol;


import javax.annotation.Nonnull;
import java.net.InetSocketAddress;
import java.util.*;

public class Server {

    public BedrockServer server;
    public BedrockPong pong;

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    public static final YAMLMapper YAML_MAPPER = (YAMLMapper) new YAMLMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private List<BCPlayer> players;

    public List<BCPlayer> getPlayers() {
        return players;
    }

    public BCPlayer getPlayer(String uuid) {
        for(int i=0;i<players.size();i++) {
            if(players.get(i) != null && players.get(i).getUuid() != null && players.get(i).getUuid() == uuid)
                return players.get(i);
        }
        return null;
    }

    public void addPlayer(BCPlayer player) {
        System.out.println("Total users connected: " + this.players.size());
        this.players.add(player);
    }

    public void removePlayer(BCPlayer player) {
        if(this.players.contains(player))
            this.players.remove(player);
    }


    public Server(String bindIp, String port) {
        Server current = this;
        players = new ArrayList<>();

        InetSocketAddress bindAddress = new InetSocketAddress(bindIp, Integer.parseInt(port));

        server = new BedrockServer(bindAddress);
        pong = new BedrockPong();
        pong.setEdition("MCPE");
        pong.setMotd(BedrockConnect.language.getWording("serverInfo", "motd"));
        pong.setSubMotd(BedrockConnect.language.getWording("serverInfo", "subMotd"));
        pong.setPlayerCount(0);
        pong.setMaximumPlayerCount(20);
        pong.setGameType("Survival");
        pong.setIpv4Port(Integer.parseInt(port));
        pong.setProtocolVersion(BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion());
        pong.setVersion(BedrockProtocol.DEFAULT_BEDROCK_CODEC.getMinecraftVersion());
        server.setHandler(new BedrockServerEventHandler() {
            @Override
            public boolean onConnectionRequest(InetSocketAddress address) {
                return true; // Connection will be accepted
            }

            @Nonnull
            public BedrockPong onQuery(InetSocketAddress address) {
                return pong;
            }

            @Override
            public void onSessionCreation(BedrockServerSession session) {
                    session.setPacketHandler(new PacketHandler(session, current, false));
            }
        });
        // Start server up
        server.bind().join();
        System.out.println("Bedrock Connection Started: " + bindIp + ":" + port);
        if(BedrockConnect.kickInactive) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i) != null && !players.get(i).isActive())
                            players.get(i).disconnect(BedrockConnect.language.getWording("disconnect", "inactivity"), current);
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 0L, 60 * 1000);
        }

        new Thread() {
            public void run() {
                Scanner sc = null;
                try {
                    sc = new Scanner(System.in);
                    while(sc.hasNextLine()) {
                        String cmd = sc.next();
                        switch(cmd) {
                            case "end":
                            case "stop":
                                System.exit(0);
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (sc != null) {
                        try {
                            sc.close();
                        } catch (Exception e1) {
                            // just ignore it
                        }
                        sc = null;
                    }
                }
            }
        }.start();

    }
}
