class NavSegment
{
	static enum NAV_COMMAND { invalid, turn, proceed };

	private NAV_COMMAND		m_command;	    // turn left, turn right, proceed
	private String          m_streetName;	// Westwood Blvd
	private String          m_direction;	// "left" for turn or "northeast" for proceed
	private double			m_distance;		// 3.2 //KM
	private GeoSegment		m_gs;

	NavSegment()
	{
		m_command = NAV_COMMAND.invalid;
	}
	
	void initTurn(String turnDirection, String streetName)
	{
		m_command = NAV_COMMAND.turn;
		m_streetName = streetName;
		m_direction = turnDirection;
		m_distance = 0;
	}

	void initProceed(String direction, String streetName, double distance, GeoSegment gs)
	{
		m_command = NAV_COMMAND.proceed;
		m_direction = direction;
		m_streetName = streetName;
		m_distance = distance;
		m_gs = gs;
	}

	NAV_COMMAND getCommandType()
	{
		return m_command;
	}
	
	String getDirection()
	{
		return m_direction;
	}

	String getStreet()
	{
		return m_streetName;
	}

	double getDistance()
	{
		return m_distance;
	}

	void setDistance(double dist)
	{
		m_distance = dist;
	}

	GeoSegment getSegment()
	{
		return m_gs;
	}
};
