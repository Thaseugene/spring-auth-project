package com.spring.auth.repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionBuilder {

    Connection takeConnection() throws SQLException;
}
