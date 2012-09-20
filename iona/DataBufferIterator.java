
package org.pt;

import java.util.Iterator;


public class DataBufferIterator implements Iterator
{

    public DataBufferIterator(DataElement top)
    {
        m_cur = top;
    }

    public boolean hasNext()
    {
        return m_cur != null;
    }

    
    public Object next()
    {
        Object obj = m_cur.getObject();
        m_cur = m_cur.getPrev();
        return obj;
    }

    public void remove()
    {
        GraphMain.error("org.pt.RingBufferIterator.remove() not implemented");
    }

    private DataElement m_cur;
}

