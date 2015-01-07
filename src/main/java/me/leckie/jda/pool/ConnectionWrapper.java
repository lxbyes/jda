package me.leckie.jda.pool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;

public class ConnectionWrapper implements PooledConnection {

	private Connection connection = null;

	private boolean busy = false;

	public ConnectionWrapper(Connection connection) {
		this.connection = connection;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return connection;
	}

	@Override
	public void close() throws SQLException {
		if (connection == null) {
			return;
		}
		if (busy) {
			try {
				Thread.sleep(5000);
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
		busy = false;
	}

	@Override
	public void addConnectionEventListener(ConnectionEventListener listener) {
		System.out.println("addConnectionEventListener");
	}

	@Override
	public void removeConnectionEventListener(ConnectionEventListener listener) {
		System.out.println("removeConnectionEventListener");
	}

	@Override
	public void addStatementEventListener(StatementEventListener listener) {
		System.out.println("addStatementEventListener");
	}

	@Override
	public void removeStatementEventListener(StatementEventListener listener) {
		System.out.println("removeStatementEventListener");
	}

}
