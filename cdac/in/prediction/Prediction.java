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
	double reasonableTime;
	double fifthPercentile;
	double fifthPercentileAction;

	List<Applicant> attemptedApplicant;
	List<Double> alltime;
	List<Integer> allAction;

	Question(String id, String type){

		this.id = id;
		this.type = type;
		this.totalTime = 0;
		this.avargeTime = 0.0d;
		this.totalAction = 0;
		this.avargeAction = 0.0d;
		this.reasonableTime = 0.0d;
		this.fifthPercentile = 0.0d;
		this.fifthPercentileAction = 0.0d;
		this.attemptedApplicant = new ArrayList<Applicant>();
		this.alltime = new ArrayList<Double>();
		this.allAction = new ArrayList<Integer>();
	}

	void print(String block){
		avargeTime =  totalTime / (double) attemptedApplicant.size() ;
		avargeAction =  totalAction / (double) attemptedApplicant.size() ;
		System.out.println(block+", "+id+", "+type+", "+ totalTime+", "+avargeTime+", "+totalAction+", "+avargeAction+", "+fifthPercentile+", "+fifthPercentileAction);
	}
}

class Block{

	String id;

	Map<String, Question> questions;
	Map<String, Question> questions20;
	Map<String, Question> questions10;
	
	Block(String id){
		this.id = id;
		this.questions = new TreeMap<String, Question>();
		this.questions20 = new TreeMap<String, Question>();
		this.questions10 = new TreeMap<String, Question>();
	}

	void calculate(){

		for(String id: questions.keySet()){

			Question question = questions.get(id);
			Collections.sort( question.alltime );
			long index =  Math.round( ( (double)5 / (double)100 ) * (double) question.alltime.size() ); 
			question.fifthPercentile = question.alltime.get( (int)index ); 

			Collections.sort( question.allAction );
			index =  Math.round( ( (double)5 / (double)100 ) * (double) question.allAction.size() ); 
			question.fifthPercentileAction = question.allAction.get( (int) index ); 

			questions.put( id, question );
		}

		for(String id: questions20.keySet()){
			Question question = questions20.get(id);
			Collections.sort( question.alltime );
			long index =  Math.round( ( (double)5 / (double)100 ) * (double) question.alltime.size() ); 
			question.fifthPercentile = question.alltime.get( (int)index ); 

			Collections.sort( question.allAction );
			index =  Math.round( ( (double)5 / (double)100 ) * (double) question.allAction.size() ); 
			question.fifthPercentileAction = question.allAction.get( (int) index ); 
			questions20.put( id, question );
		}

		for(String id: questions10.keySet()){

			Question question = questions10.get(id);
			Collections.sort( question.alltime );
			long index =  Math.round( ( (double)5 / (double)100 ) * (double) question.alltime.size() ); 
			question.fifthPercentile = question.alltime.get( (int)index ); 

			Collections.sort( question.allAction );
			index =  Math.round( ( (double)5 / (double)100 ) * (double) question.allAction.size() ); 
			question.fifthPercentileAction = question.allAction.get( (int) index ); 
			questions10.put( id, question );
		}

	}

	void print(){

		System.out.println("Block, QuestionId, QuestionType, TotalTime, AvargeTime, TotalActionTaken, AvargeActionTaken, 5th-Percentile, 5th-PercentileAction");
		for(String id: questions.keySet()){
			questions.get(id).print( id );
		}
		System.out.println("Block, 20QuestionId, QuestionType, TotalTime, AvargeTime, TotalActionTaken, AvargeActionTaken, 5th-Percentile, 5th-PercentileAction");
		for(String id: questions20.keySet()){
			questions20.get(id).print( id );
		}
		System.out.println("Block, 10QuestionId, QuestionType, TotalTime, AvargeTime, TotalActionTaken, AvargeActionTaken, 5th-Percentile, 5th-PercentileAction");
		for(String id: questions10.keySet()){
			questions10.get(id).print( id );
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
	int actionresult;

	Response(String id, String type, String blockId){

		this.questionId = questionId;
		this.type = type;
		this.blockId = blockId;
		this.enterTime = null;
		this.exitTime = null;	
		this.timeTaken = 0.0d;
		this.result = 0;
		this.actionresult = 0;

		this.actions = new ArrayList<String>();
	}
}

class Applicant{

	String id;

	Map<String, Response> responses; 
	Map<String, Response> responses20;
	Map<String, Response> responsesLast10;

	Map<String,Integer> blockResult;

	int BlockRev;
        int EOSTimeLft;
    	int SecTimeOut;
        int HELPMAT8;

	int BlockRev20;
        int EOSTimeLeft20;
    	int SecTimeOut20;
        int HELPMAT820;

	int BlockRev10;
        int EOSTimeLeft10;
    	int SecTimeOut10;
        int HELPMAT810;


	Applicant(String id){

		this.id  = id;
		this.responses = new LinkedHashMap<String, Response>();
		this.responses20 = new LinkedHashMap<String, Response>();
		this.responsesLast10 = new LinkedHashMap<String, Response>();

		this.blockResult = new TreeMap<String, Integer>();

		BlockRev = 0;
		EOSTimeLft = 0;
		SecTimeOut = 0;
		HELPMAT8 = 0;

		BlockRev20 = 0;
		EOSTimeLeft20 = 0;
		SecTimeOut20 = 0;
		HELPMAT820 = 0;

		BlockRev10 = 0;
		EOSTimeLeft10 = 0;
		SecTimeOut10 = 0;
	}
}

class Data{

	Map<String, Block> blocks;
	Map<String, Applicant> applicants;

	Data(){
		blocks = new TreeMap<String, Block>();
		applicants  =  new TreeMap<String, Applicant>();
	}

	void printTariningData(){
		
	}

	void readTarget(String filename, boolean header){

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
				String result =  token[1].trim();

				Applicant applicant = applicants.get( applicantId );
				if( result.equals("TRUE"))
					applicant.blockResult.put("B", 1);
				else
					applicant.blockResult.put("B", 0);

				applicants.put( applicantId, applicant);
			}

			System.err.println("Target Read: "+count);

		}catch(Exception e){
			e.printStackTrace();
		}

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
			System.err.println("Total Data Read: "+count+" applicant Count: "+applicants.size());

		}catch(Exception e){
			System.out.println("Line: '"+line.trim()+" '");
			e.printStackTrace();
		}
	}

	void create(){

		for(String appId: applicants.keySet() ){

			Applicant applicant = applicants.get( appId );

			double timeTaken = 0;

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
						}

						timeTaken += response.timeTaken;

						question.totalTime += response.timeTaken;
						question.totalAction += response.actions.size();
						question.attemptedApplicant.add( applicant );
						question.alltime.add( response.timeTaken );
						question.allAction.add( response.actions.size() );

						block.questions.put( repId, question );

						if( timeTaken <= 20 ){
							Question q = block.questions20.get( repId );
							if( q == null ){
								q = new Question( repId, response.type );
							}	
							q.totalTime += response.timeTaken;
							q.totalAction += response.actions.size();
							q.attemptedApplicant.add( applicant );
							q.alltime.add( response.timeTaken );
							q.allAction.add( response.actions.size() );

							block.questions20.put( repId, q);
							applicant.responses20.put( repId, response );	

						}else{
							Question q = block.questions10.get( repId );
							if( q == null ){
								q = new Question( repId, response.type );
							}	
							q.totalTime += response.timeTaken;
							q.totalAction += response.actions.size();
							q.attemptedApplicant.add( applicant );
							q.alltime.add( response.timeTaken );
							q.allAction.add( response.actions.size() );

							block.questions10.put( repId, q);
							applicant.responses20.put( repId, response );	
						}

					}else if ( repId.equals("BlockRev")  ){

						applicant.BlockRev++;

						if( timeTaken <= 20 ){
							applicant.BlockRev20++;
						}else{
							applicant.BlockRev10++;
						}

					}else if ( repId.equals("EOSTimeLft")  ){

						applicant.EOSTimeLft++;

						if( timeTaken <= 20 ){
							applicant.EOSTimeLeft20++;
						}else{
							applicant.EOSTimeLeft10++;
						}

					}else if ( repId.equals("SecTimeOut")  ){

						applicant.SecTimeOut++;

						if( timeTaken <= 20 ){
							applicant.SecTimeOut20++;
						}else{
							applicant.SecTimeOut10++;
						}

					}else if ( repId.equals("HELPMAT8")  ){

						applicant.HELPMAT8++;
						if( timeTaken <= 20 ){
							applicant.HELPMAT820++;
						}else{
							applicant.HELPMAT810++;
						}
					} 	
				}else if ( response.blockId.equals("B")  ){

				}

				blocks.put( response.blockId, block  );
				applicant.responses.put( repId, response );
			}
			applicants.put( appId, applicant );
		}
	}

	void calculateApplicant(){

		Block block = blocks.get("A");

		for(String id: applicants.keySet() ){

			Applicant applicant = applicants.get( id ); 
			int count = 0;
			for(String qId: applicant.responses.keySet() ){

				Response response = applicant.responses.get( qId );

				if(  qId.substring(0,2).equals("VH") ){
					Question question = block.questions.get( qId );

					if( response.timeTaken >=  question.fifthPercentile ){
						response.result = 1;
						count++;
					}

					if( response.actions.size() >=  question.fifthPercentileAction ){
						response.actionresult = 1;
					}
				}
				applicant.responses.put(qId, response );
			}
			if( count == block.questions.size() ){
				applicant.blockResult.put("A", 1);
			}else{
				applicant.blockResult.put("A", 0);
			}
			applicants.put( id, applicant );
		}
	}


	void calculate5thPecentile(){
		Block block = blocks.get("A");
		block.calculate();
		calculateApplicant();
	}

	void print(){
		Block block = blocks.get("A");
		System.out.println( "Total Applicant: "+applicants.size() );
		block.print();
	}
}

class Prediction{

	Data data;

	Prediction(){

		data = new Data();
	}

	void readData(String filename, boolean header){
		data.read(filename, true);
	}
	void readDataTarget(String filename, boolean header){
		data.readTarget(filename, true);
	}
	
	void createData(){
		data.create();	
		data.calculate5thPecentile();
	}

	void printData(){
		data.print();
	}	
	
	
	public static void main(String[] args){

		String filename = null;
		String tarfilename = null;
		int i = 0;

		while( i < args.length ){

			if( args[i].equals("-f") ){
				filename = args[i+1].trim();
			}
			if( args[i].equals("-tf") ){
				tarfilename = args[i+1].trim();
			}
			i++;
		}

		if( filename == null || tarfilename == null){

			System.err.println("Uses: -f <training-data>");
			System.err.println("Uses: -tf <target-data>");
			System.exit(0);
		}
	
		Prediction prediction = new Prediction();
		prediction.readData( filename, true );
		prediction.readDataTarget( tarfilename, true );
		prediction.createData();
		prediction.printData();
	}
}
