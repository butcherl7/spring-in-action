package top.funsite.spring.action.shiro.session;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * 未登录情况下的空 session.
 */
public class BlankSession implements Session {

    private static final BlankSession BLANK_SESSION = new BlankSession();

    private static final Date FINAL_DATE = new Date(0L);

    public static final String ID = "";

    private BlankSession() {
    }

    public static Session getInstance() {
        return BLANK_SESSION;
    }

    @Override
    public Serializable getId() {
        return ID;
    }

    @Override
    public Date getStartTimestamp() {
        return FINAL_DATE;
    }

    @Override
    public Date getLastAccessTime() {
        return FINAL_DATE;
    }

    @Override
    public long getTimeout() throws InvalidSessionException {
        return 0;
    }

    @Override
    public void setTimeout(long maxIdleTimeInMillis) throws InvalidSessionException {
    }

    @Override
    public String getHost() {
        return "unknown";
    }

    @Override
    public void touch() throws InvalidSessionException {
    }

    @Override
    public void stop() throws InvalidSessionException {
    }

    @Override
    public Collection<Object> getAttributeKeys() throws InvalidSessionException {
        return Collections.emptyList();
    }

    @Override
    public Object getAttribute(Object key) throws InvalidSessionException {
        return null;
    }

    @Override
    public void setAttribute(Object key, Object value) throws InvalidSessionException {
    }

    @Override
    public Object removeAttribute(Object key) throws InvalidSessionException {
        return null;
    }
}
