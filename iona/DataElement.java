package org.pt;

public class DataElement
{
    public DataElement(Object obj, DataElement prev)
    {
        m_obj  = obj;
        m_next = null;
        m_prev = prev;
    }

    Object getObject()
    {
        return m_obj;
    }
    
    void setNext(DataElement next)
    {
        m_next = next;
    }

    DataElement getNext()
    {
        return m_next;
    }
    
    DataElement getPrev()
    {
        return m_prev;
    }
    
    void chop()
    {
        m_prev = null;
    }
      

    private Object      m_obj;
    private DataElement m_next;
    private DataElement m_prev;

}
