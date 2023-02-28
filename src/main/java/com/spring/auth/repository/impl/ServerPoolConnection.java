package com.spring.auth.repository.impl;

import com.spring.auth.repository.ConnectionBuilder;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ServerPoolConnection implements ConnectionBuilder {

    private static final Logger LOGGER = Logger.getLogger(ServerPoolConnection.class.getName());
    private final DataSource dataSource;

    private ServerPoolConnection() {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/springAuthProject");
        } catch (NamingException e) {
            LOGGER.log(Level.INFO, "Error while initialize connection", e);
            throw new RuntimeException();
        }
    }

    @Override
    public Connection takeConnection() throws SQLException {
        return dataSource.getConnection();
    }


}
