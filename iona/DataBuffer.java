package org.pt;


public class DataBuffer
{
    public DataBuffer(int size)
    {
        m_size   = size;
        m_nelems = 0;
        m_top    = null;
        m_tail   = null;
    }
    

    public void add(Object obj)
    {
        DataElement de = new DataElement(obj, m_top);
        if (m_top != null)
        {
            m_top.setNext(de);
        }
        else
        {
            m_tail = de;
        }

        m_top = de;

        if (m_nelems == m_size)
        {
            m_tail = m_tail.getNext();
            m_tail.chop();
        }
        else
        {
            m_nelems++;
        }
    }

    public DataBufferIterator getIterator()
    {
        return new DataBufferIterator(m_top);
    }

    public void resize(int size)
    {
        if (size <= m_size)
        {
            return;
        }
        m_size = size;
    }


    private int         m_size;
    private int         m_nelems;
    private DataElement m_top;
    private DataElement m_tail;
}
