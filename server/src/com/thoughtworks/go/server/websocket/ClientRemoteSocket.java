/*
 * Copyright 2017 ThoughtWorks, Inc.
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
package com.thoughtworks.go.server.websocket;

import com.google.gson.Gson;
import com.thoughtworks.go.websocket.Message;
import com.thoughtworks.go.websocket.MessageEncoding;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.ByteBuffer;

@WebSocket
public class ClientRemoteSocket {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRemoteSocket.class);
    private ClientRemoteHandler handler;
    private Session session;
    private Gson gson = new Gson();

    public ClientRemoteSocket(ClientRemoteHandler handler) {
        this.handler = handler;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        LOGGER.debug("{} connected.", sessionName());
    }

    @OnWebSocketMessage
    public void onMessage(String input) throws Exception {
        BuildParams params = gson.fromJson(input, BuildParams.class);
        LOGGER.debug("{} message: {}", sessionName(), params);
        handler.process(this, params);
    }

    @OnWebSocketClose
    public void onClose(int closeCode, String closeReason) {
        LOGGER.debug("{} closed. code: {}, reason: {}", sessionName(), closeCode, closeReason);
    }

    @OnWebSocketError
    public void onError(Throwable error) {
        LOGGER.error(sessionName() + " error", error);
    }

    @OnWebSocketFrame
    public void onFrame(Frame frame) {
        LOGGER.debug("{} receive frame: {}", sessionName(), frame.getPayloadLength());
    }

    public void send(String msg) {
        LOGGER.debug("{} send message: {}", sessionName(), msg);
        session.getRemote().sendStringByFuture(msg);
    }

    private String sessionName() {
        return session == null ? "[No session initialized]" : "Session[" + session.getRemoteAddress() + "]";
    }

    @Override
    public String toString() {
        return "[ClientRemoteSocket: " + sessionName() + "]";
    }
}
