package uk.co.n3tw0rk.websocketregistration;

import uk.co.n3tw0rk.websocketregistration.framing.DataFrameRequest;
import uk.co.n3tw0rk.websocketregistration.threads.SocketServer;
import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public class Main extends Abstraction
{
	public Main()
	{
		try
		{

			DataFrameRequest dataFrameRequest = new DataFrameRequest();
			/*
			
			// Request Packet Received : f46d04005f840016d365d0500800450000321a7a400080065cf5c0a80104c0a8010205181f91ea176d42fd566f0350187f8abd0c000081845c44e6816d76d5b5
			// Data : 81 84 5c 44 e6 81 6d 76 d5 b5
			
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

			// Entire Response Packet Sent : 
			// 	00 16 d3 65 d0 50 f4 6d 
			// 	04 00 5f 84 08 00 45 00 
			//	00 32 5f e3 40 00 80 06 
			//	17 8c c0 a8 01 02 c0 a8 
			//	01 04 1f 91 05 18 fd 56 
			//	6f 03 ea 17 6d 4c 50 18 
			//	01 00 c0 fa 00 00 81 84 
			//	b2 3f de 11 83 0d ed 25

			// Data : 
			//		81 84 B2 3F DE 11 83 0D ED 25

				FIN, RSV 1-3 and Op Code :

				0x81	1000 0001
					FIN 	1 
					RSV1 	0
					RSV2	0
					RSV3	0
					Opcode	1
				
				Mask and Payload Length :
				
				0x84	1000 0100
					Mask 	1
					Length	4
	
				Mask Key :
				
				0xB2	1011 0010
				0x3F	0011 1111
				0xDE	1101 1110
				0x11	0001 0001
				
				Masked Payload Data : 

				0x83	1000 0011
				0x0D	0000 1101
				0xED	1110 1101
				0x25	0010 0101

				Unmasked Data
				
				Binary			Decimal			Hex			UTF-8
				
				1011 0010  
				1000 0011 ^
				-----------
				0011 0001		49				31			1
				
				0011 1111 
				0000 1101 ^
				-----------
				0011 0010		50				32			2
				
				1101 1110
				1110 1101 ^
				-----------
				0011 0011		51				33			3
				
				0001 0001 
				0010 0101 ^
				-----------
				0011 0100		52				34			4

			// WHY DOESN'T THE CLIENT LIKE THE DATA!?!?!??!?!? IT'S FUCKING LEGIT

			dataFrameRequest = new DataFrameRequest();
			
			// Data : 81 84 B2 3F DE 11 83 0D ED 25
			dataFrameRequest.setFrameData( 0x81 );
			dataFrameRequest.setFrameData( 0x84 );
			dataFrameRequest.setFrameData( 0xB2 );
			dataFrameRequest.setFrameData( 0x3F );
			dataFrameRequest.setFrameData( 0xDE );
			dataFrameRequest.setFrameData( 0x11 );
			dataFrameRequest.setFrameData( 0x83 );
			dataFrameRequest.setFrameData( 0x0D );
			dataFrameRequest.setFrameData( 0xED );
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
