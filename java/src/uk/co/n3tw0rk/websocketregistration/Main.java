package uk.co.n3tw0rk.websocketregistration;

// import uk.co.n3tw0rk.websocketregistration.framing.DataFrameRequest;
import uk.co.n3tw0rk.websocketregistration.threads.SocketServer;
import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public class Main extends Abstraction
{
	public Main()
	{
		try
		{
			/*
			
			// Request Packet Received : f46d04005f840016d365d0500800450000321a7a400080065cf5c0a80104c0a8010205181f91ea176d42fd566f0350187f8abd0c000081845c44e6816d76d5b5
			// Data : 81 84 5c 44 e6 81 6d 76 d5 b5

			DataFrameRequest dataFrameRequest = new DataFrameRequest();
			
			dataFrameRequest.setFrameData( 0x81 );
			dataFrameRequest.setFrameData( 0x84 );
			dataFrameRequest.setFrameData( 0x5c );
			dataFrameRequest.setFrameData( 0x44 );
			dataFrameRequest.setFrameData( 0xe6 );
			dataFrameRequest.setFrameData( 0x81 );
			dataFrameRequest.setFrameData( 0x6d );
			dataFrameRequest.setFrameData( 0x76 );
			dataFrameRequest.setFrameData( 0xd5 );
			dataFrameRequest.setFrameData( 0xb5 );
			
			console( dataFrameRequest.getPayload() ); // 1234

			// Response Packet Sent : 0016d365d050f46d04005f840800450000325fe340008006178cc0a80102c0a801041f910518fd566f03ea176d4c50180100c0fa00008184b23fde11830ded25
			// Data : 81 84 b2 3f de 11 83 0d ed 25
			// WHY DOESN'T THE CLIENT LIKE THE DATA!?!?!??!?!? IT'S FUCKING LEGIT

			dataFrameRequest = new DataFrameRequest();
			
			dataFrameRequest.setFrameData( 0x81 );
			dataFrameRequest.setFrameData( 0x84 );
			dataFrameRequest.setFrameData( 0xb2 );
			dataFrameRequest.setFrameData( 0x3f );
			dataFrameRequest.setFrameData( 0xde );
			dataFrameRequest.setFrameData( 0x11 );
			dataFrameRequest.setFrameData( 0x83 );
			dataFrameRequest.setFrameData( 0x0d );
			dataFrameRequest.setFrameData( 0xed );
			dataFrameRequest.setFrameData( 0x25 );
			
			console( dataFrameRequest.getPayload() ); // 1234
			
			*/

			( new SocketServer( 8081 ) ).start();
			this.infinite();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public void infinite()
	{
		while( true )
		{
			try
			{
				Thread.sleep( 10000 );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main( String [] args )
	{
		new Main();
	}

}
