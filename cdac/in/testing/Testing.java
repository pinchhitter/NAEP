package cdac.in.testing;

import java.io.*;
import java.util.*;
import java.sql.Timestamp;

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
		//System.out.println(block+", "+id+", "+type+", "+ totalTime+", "+avargeTime+", "+totalAction+", "+avargeAction+", "+fifthPercentile+", "+fifthPercentileAction);
	}
}

class Block{

	String id;

	Map<String, Question> questions;
	
	Block(String id){

		this.id = id;
		this.questions = new TreeMap<String, Question>();
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
	}

	void print(){

		/*
		System.out.println("Block, QuestionId, QuestionType, TotalTime, AvargeTime, TotalActionTaken, AvargeActionTaken, 5th-Percentile, 5th-PercentileAction");
		for(String id: questions.keySet()){
			questions.get(id).print( id );
		}
		*/
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
	String type;

	Map<String, Response> responses; 
	Map<String,Integer> blockResult;

	int BlockRev;
        int EOSTimeLeft;
    	int SecTimeOut;
        int HELPMAT8;

	Applicant(String id, String type){

		this.id  = id;
		this.type = type;
		this.responses = new LinkedHashMap<String, Response>();
		this.blockResult = new TreeMap<String, Integer>();

		BlockRev = 0;
		EOSTimeLeft = 0;
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

	void printTestingHeader(){

		System.out.println("@relation 'NAEP TRAINING DATA SET'");
		System.out.println("@attribute time {0.3,0.2,0.1}");
		Block block = blocks.get("A");
		for(String qid: block.questions.keySet() ){
			System.out.println("@attribute "+qid+"time numeric");
			//System.out.println("@attribute "+qid+"Action numeric");
			System.out.println("@attribute "+qid+"ETU {0,1}");
			//System.out.println("@attribute "+qid+"ActionResult {0,1}");
		}
		/*
		System.out.println("@attribute BlockRev numeric ");
		System.out.println("@attribute EOSTimeLft numeric");
		System.out.println("@attribute SecTimeOut numeric");
		System.out.println("@attribute HELPMAT8 numeric");
		*/
		System.out.println("@data");
	}

	void printTestingData(){

		Block block = blocks.get("A");

		for(String appId: applicants.keySet() ){

			Applicant applicant = applicants.get( appId );

			System.out.print( applicant.type );

			for( String qId: block.questions.keySet() ){

				Response res = applicant.responses.get( qId );

				if( res != null ){
					System.out.print(", "+res.timeTaken+", "+res.result);
				}else{
					System.out.print(", 0, 0");
				}
			}
			System.out.println(", "+applicant.BlockRev+", "+applicant.EOSTimeLeft+", "+applicant.SecTimeOut+", "+applicant.HELPMAT8);
		}
	}

	void readTestingData(String filename, boolean header, String time){

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

				Applicant applicant = null; 
				applicant = applicants.get( applicantId );

				if( applicant == null){
					applicant = new Applicant( applicantId, time );
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
				try{
					Timestamp et = Timestamp.valueOf( response.enterTime);
					Timestamp xt = Timestamp.valueOf( response.exitTime );

					long milliseconds = xt.getTime() - et.getTime();
					double seconds = (double) milliseconds / 1000;
					response.timeTaken = (float) ( seconds % 3600 ) / (double) 60 ;

				}catch(Exception e){
					response.timeTaken = 0;
				}

				System.err.println( appId+", "+repId+", ET: "+response.enterTime+" XT:"+response.exitTime+" TT:"+response.timeTaken);

				if( response.blockId.equals("A") ){

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


					}else if ( repId.equals("BlockRev")  ){
						applicant.BlockRev = 1;
					}else if ( repId.equals("EOSTimeLft")  ){
						applicant.EOSTimeLeft= 1;
					}else if ( repId.equals("SecTimeOut")  ){
						applicant.SecTimeOut = 1;
					}else if ( repId.equals("HELPMAT8")  ){
						applicant.HELPMAT8 = 1;
					} 	
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
			if( count == block.questions.size() )
				applicant.blockResult.put("A",1); 

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
		block.print();
	}
}

class Testing{

	Data data;

	Testing(){
		data = new Data();
	}

	void readTestingData(String filename, boolean header,String time){
		data.readTestingData(filename, header, time);
	}

	void createData(){
		data.create();	
		data.calculate5thPecentile();
	}

	void printData(){
		data.print();
		data.printTestingHeader();
		data.printTestingData();
	}	

	public static void main(String[] args){

		Testing testing = new Testing();

		testing.readTestingData( "../data/data_a_hidden_30.csv", true, "0.3");
		testing.readTestingData( "../data/data_a_hidden_20.csv", true, "0.2");
		testing.readTestingData( "../data/data_a_hidden_10.csv", true, "0.1");

		testing.createData();
		testing.printData();
	}
}
