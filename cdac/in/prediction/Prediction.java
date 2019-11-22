package cdac.in.prediction;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;

class Question{

	String id;
	String type;
	double totalTime;
	double avargeTime;
	int totalAction;
	double avargeAction;
	List<Applicant> attemptedApplicant;

	Question(String id, String type){

		this.id = id;
		this.type = type;
		this.totalTime = 0;
		this.avargeTime = 0.0d;
		this.totalAction = 0;
		this.avargeAction = 0.0d;
		this.attemptedApplicant = new ArrayList<Applicant>();
	}

	void print(String block){

		avargeTime =  totalTime / (double) attemptedApplicant.size() ;
		avargeAction =  totalAction / (double) attemptedApplicant.size() ;

		System.out.println(block+", "+id+", "+type+", "+ totalTime+", "+avargeTime+", "+totalAction+", "+avargeAction);
	}
}

class Block{

	String id;

	Map<String, Question> questions;
	
	Block(String id){
		this.id = id;
		this.questions = new TreeMap<String, Question>();
	}

	void print(){

		System.out.println("Block, QuestionId, QuestionType, TotalTime, AvargeTime, TotalActionTaken, AvargeActionTaken");

		for(String id: questions.keySet()){
			questions.get(id).print( id );
		}
	}
}

class Response{

	String questionId;
	String blockId;
	String type;
	String enterTime;
	String exitTime;
	double timeTaken;
	List<String> actions;
	int result;

	Response(String id, String type, String blockId){

		this.questionId = questionId;
		this.type = type;
		this.blockId = blockId;
		this.enterTime = null;
		this.exitTime = null;	
		this.timeTaken = 0.0d;
		this.result = 0;

		this.actions = new ArrayList<String>();
	}
}

class Applicant{

	String id;
	Map<String, Response> responses; 
	Map<String,String> blockResult;
	int BlockRev;
        int EOSTimeLft;
    	int SecTimeOut;
        int HELPMAT8;


	Applicant(String id){

		this.id  = id;
		this.responses = new TreeMap<String, Response>();
		this.blockResult = new TreeMap<String, String>();
		BlockRev = 0;
		EOSTimeLft = 0;
		SecTimeOut = 0;
		HELPMAT8 = 0;
	}
}

class Data{

	Map<String, Block> blocks;
	Map<String, Applicant> applicants;

	Data(){
		blocks = new TreeMap<String, Block>();
		applicants  =  new TreeMap<String, Applicant>();
	}

	void read(String filename, boolean header){

		BufferedReader br = null;
		String line = null;
		int count = 0;

		try{
			br = new BufferedReader( new FileReader( new File(filename) ) );

			while( ( line = br.readLine()) != null ){

				if( header ){
					header = false;
					continue;
				}
				count++;
				String[] token =  line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				String applicantId =  token[0].trim();
				String blockId =  token[1].trim();
				String qId =  token[2].trim();
				String qType =  token[3].trim();
				String action =  token[4].trim();
				String info =  token[5].trim();
				String eventTime =  token[6].trim();


				Applicant applicant = applicants.get( applicantId );

				if( applicant == null){
					applicant = new Applicant( applicantId );
				}


				if( blockId.equals("A") ){	

					Response response = applicant.responses.get( qId );

					if( response == null){
						response = new Response( qId, qType, blockId );
					}

					if( action.equals("Enter Item") ){

						response.enterTime = eventTime;

					}else if ( action.equals("Exit Item") ){

						response.exitTime = eventTime;

					}else{
						if( response.enterTime == null ){
							response.enterTime = eventTime;
						}else if ( response.exitTime == null ){
							response.exitTime = eventTime;
						}	
						response.actions.add( info );
					}

					applicant.responses.put( qId, response );
					applicants.put( applicantId, applicant );

				}else{

				}
			} 
			System.err.println("Total Data Read: "+count);

		}catch(Exception e){
			System.out.println("Line: '"+line.trim()+" '");
			e.printStackTrace();
		}
	}

	void create(){

		for(String appId: applicants.keySet() ){

			Applicant applicant = applicants.get( appId );

			for(String repId: applicant.responses.keySet() ){

				Response response = applicant.responses.get( repId );

				Block block = blocks.get( response.blockId );

				if( block == null ){
					block = new Block( response.blockId );
				}

				if( response.blockId.equals("A") ){

					String []et = response.enterTime.split(":");
					String []xt = response.exitTime.split(":");

					double unit = 60.0d;

					if( Integer.parseInt( xt[0] ) < Integer.parseInt( et[0] ) ){

						double min = ( (double) 60 - (double) Integer.parseInt( et[0] ) )/60 + (double) Integer.parseInt( xt[0] );
						double sec = ( 60 - Double.parseDouble( et[1].trim() ) ) + Double.parseDouble( xt[1].trim() );
						response.timeTaken = ( (min + (int) sec / 60   ) * unit + (sec % unit )  ) / unit; 		

					}else if ( Integer.parseInt( xt[0] ) > Integer.parseInt( et[0] )  ) {

						double min = (Integer.parseInt( xt[0] ) ) - Integer.parseInt( et[0] );
						double sec = ( 60 - Double.parseDouble( et[1].trim() ) ) + Double.parseDouble( xt[1].trim() );
						response.timeTaken = ( (min + (int) sec / 60  ) * unit + (sec % unit )  ) / unit; 		

					}else {
						double min =0.0d;
						double sec =  Double.parseDouble( xt[1].trim() ) - Double.parseDouble( et[1].trim() );
						response.timeTaken = ( sec / unit ) ; 		
					}

					System.err.println( appId+", "+repId+", ET: "+response.enterTime+" XT:"+response.exitTime+" TT:"+response.timeTaken);

					if(  repId.substring(0,2).equals("VH") ){

						Question question = block.questions.get( repId );

						if( question == null ){
							question = new Question( repId, response.type );
							question.totalTime += response.timeTaken;
							question.totalAction += response.actions.size();
							question.attemptedApplicant.add( applicant );
						}	

						block.questions.put( repId, question );

					}else if ( repId.equals("BlockRev")  ){
						applicant.BlockRev++;
					}else if ( repId.equals("EOSTimeLft")  ){
						applicant.EOSTimeLft++;
					}else if ( repId.equals("SecTimeOut")  ){
						applicant.SecTimeOut++;
					}else if ( repId.equals("HELPMAT8")  ){
						applicant.HELPMAT8++;
					} 	
				}else if ( response.blockId.equals("B")  ){

				}

				blocks.put( response.blockId, block  );
				applicant.responses.put( repId, response );
			}
			applicants.put( appId, applicant );
		}

	}

	void print(){
		
	}
}

class Prediction{

	Data data;

	Prediction(){
		data = new Data();
	}

	void readData(String filename, boolean header){
		System.err.println("Here I am");
		data.read(filename, true);
	}
	
	void createData(){
		data.create();	
	}

	void printData(){
		data.print();
	}	
	
	
	public static void main(String[] args){

		String filename = null;
		int i = 0;
		while( i < args.length ){
			if( args[i].equals("-f") ){
				filename = args[i+1].trim();
			}
			i++;
		}

		if( filename == null){
			System.err.println("Uses: -f <training-data>");
			System.exit(0);
		}
	
		Prediction prediction = new Prediction();
		prediction.readData( filename, true );
		prediction.createData();
		prediction.printData();
	}
}
