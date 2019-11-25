package cdac.in.training;

import java.io.*;
import java.util.*;
import java.time.*;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	Map<String, Question> questions20;
	Map<String, Question> questions10;
	
	Set<String> qType;
	
	Block(String id){

		this.id = id;
		this.questions = new TreeMap<String, Question>();
		this.questions20 = new TreeMap<String, Question>();
		this.questions10 = new TreeMap<String, Question>();
		this.qType = new HashSet<String>();
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

		/*

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

	Map<String, Response> responses; 
	Map<String, Response> responses20;
	Map<String, Response> responses10;

	Map<String, Integer> qType;
	Map<String, Integer> qType20;
	Map<String, Integer> qType10;

	Map<String,Integer> blockResult;

	int BlockRev;
        int EOSTimeLeft;
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
		this.responses10 = new LinkedHashMap<String, Response>();

		this.qType = new TreeMap<String,Integer>();
		this.qType20 = new TreeMap<String,Integer>();
		this.qType10 = new TreeMap<String,Integer>();

		this.blockResult = new TreeMap<String, Integer>();

		BlockRev = 0;
		EOSTimeLeft = 0;
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

	void printTrainingHeader(){

		System.out.println("@relation 'NAEP TRAINING DATA SET'");
		System.out.println("@attribute time {0.3,0.2,0.1}");
		Block block = blocks.get("A");

		for(String qid: block.questions.keySet() ){

			System.out.println("@attribute "+qid+"time numeric");
			System.out.println("@attribute "+qid+"ETU {0,1}");
		}
		for(String qid: block.qType ){
			System.out.println("@attribute "+qid+"-Count numeric");
		}

		System.out.println("@attribute BlockRev numeric ");
		System.out.println("@attribute EOSTimeLft numeric");
		System.out.println("@attribute SecTimeOut numeric");
		System.out.println("@attribute HELPMAT8 numeric");
		/*
		System.out.println("@attribute BlockA {0,1}");
		*/
		System.out.println("@attribute class {0,1}");
		System.out.println("@data");
	}

	void printTrainingData(String time){

		Block block = blocks.get("A");

		for(String appId: applicants.keySet() ){

			Applicant applicant = applicants.get( appId );

			if( time.equals("0.3") ){
				System.out.print("0.3");
			}else if( time.equals("0.2") ){
				System.out.print("0.2");
			}else if( time.equals("0.1") ){
				System.out.print("0.1");
			}

			for( String qId: block.questions.keySet() ){

				Response res = null ;

				if( time.equals("0.3") ){
					res = applicant.responses.get( qId );
				}else if( time.equals("0.2") ){
					res = applicant.responses20.get( qId );
				}else if( time.equals("0.1") ){
					res = applicant.responses10.get( qId );
				}

				if( res != null ){
					System.out.print(", "+res.timeTaken+", "+res.result);
				}else{
					System.out.print(",0, 0");
				}
			}
			for(String qId: block.qType ){
				Integer count = 0;
				if( time.equals("0.3") ){
					count = applicant.qType.get( qId );
				}else if( time.equals("0.2") ){
					count = applicant.qType20.get( qId );
				}else if( time.equals("0.1") ){
					count = applicant.qType10.get( qId );
				}
				if( count == null)
					System.out.print(", 0");
				else
					System.out.print(", "+count);
			}

			if( time.equals("0.3") ){
				System.out.print(", "+applicant.BlockRev+", "+applicant.EOSTimeLeft+", "+applicant.SecTimeOut+", "+applicant.HELPMAT8);
			}else if ( time.equals("0.2") ){
				System.out.print(", "+applicant.BlockRev20+", "+applicant.EOSTimeLeft20+", "+applicant.SecTimeOut20+", "+applicant.HELPMAT820);
			}else if ( time.equals("0.1")){
				System.out.print(", "+applicant.BlockRev10+", "+applicant.EOSTimeLeft10+", "+applicant.SecTimeOut10+", "+applicant.HELPMAT810);
			}
			/*
			System.out.print(", "+applicant.blockResult.get("A"));
			*/
			System.out.println(", "+applicant.blockResult.get("B"));
		}
	}

	void readTrainingTarget(String filename, boolean header){

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

				if( result.equals("True"))
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

	void readTraingData(String filename, boolean header){

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

					try{
						Timestamp et = Timestamp.valueOf( response.enterTime);
						Timestamp xt = Timestamp.valueOf( response.exitTime );
						long milliseconds = xt.getTime() - et.getTime();
						double seconds = (double) milliseconds / 1000;
						response.timeTaken = (float) ( seconds % 3600 ) / (double) 60 ;

					}catch(Exception e){

						response.timeTaken = 0.0d;
					}	

					//System.err.println( appId+", "+repId+", ET: "+response.enterTime+" XT:"+response.exitTime+" TT:"+response.timeTaken);

					if(  repId.substring(0,2).equals("VH") ){

						Question question = block.questions.get( repId );
						block.qType.add( response.type );

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

							applicant.responses20.put( repId, response );	
							question = block.questions20.get( repId );
							if( question == null ){
								question = new Question( repId, response.type );
							}
							question.totalTime += response.timeTaken;
							question.totalAction += response.actions.size();
							question.attemptedApplicant.add( applicant );
							question.alltime.add( response.timeTaken );
							question.allAction.add( response.actions.size() );
							block.questions20.put( repId, question );
						}else{
							applicant.responses10.put( repId, response );	
							question = block.questions10.get( repId );
							if( question == null ){
								question = new Question( repId, response.type );
							}
							question.totalTime += response.timeTaken;
							question.totalAction += response.actions.size();
							question.attemptedApplicant.add( applicant );
							question.alltime.add( response.timeTaken );
							question.allAction.add( response.actions.size() );
							block.questions10.put( repId, question );
						}

					}else if ( repId.equals("BlockRev")  ){

						applicant.BlockRev = 1;

						if( timeTaken <= 20 ){
							applicant.BlockRev20 = 1;
						}else{
							applicant.BlockRev10 = 1;
						}

					}else if ( repId.equals("EOSTimeLft")  ){

						applicant.EOSTimeLeft= 1;

						if( timeTaken <= 20 ){
							applicant.EOSTimeLeft20 = 1;
						}else{
							applicant.EOSTimeLeft10 = 1;
						}

					}else if ( repId.equals("SecTimeOut")  ){

						applicant.SecTimeOut = 1;

						if( timeTaken <= 20 ){
							applicant.SecTimeOut20 = 1;
						}else{
							applicant.SecTimeOut10 = 1;
						}

					}else if ( repId.equals("HELPMAT8")  ){

						applicant.HELPMAT8 = 1;
						if( timeTaken <= 20 ){
							applicant.HELPMAT820 = 1;
						}else{
							applicant.HELPMAT810 = 1;
						}
					} 	
				}
				blocks.put( response.blockId, block  );
				applicant.responses.put( repId, response );
			}

			applicants.put( appId, applicant );
		}
	}

	void calculateApplicant(String time){

		Block block = blocks.get("A");

		for(String id: applicants.keySet() ){

			Applicant applicant = applicants.get( id ); 

			int count = 0;

			Map<String,Response> responses = null;
			Map<String,Question> questions = null;
			Map<String,Integer> qType = null;

			if( time.equals("3.0") ){
				responses = applicant.responses;
				questions = block.questions;
				qType = applicant.qType;
				
			}else if( time.equals("2.0") ){
				responses = applicant.responses20;
				questions = block.questions20;
				qType = applicant.qType20;
			}else if( time.equals("1.0") ){
				responses = applicant.responses10;
				questions = block.questions10;
				qType = applicant.qType10;
			}
			
			for(String qId: responses.keySet() ){

				Response response = responses.get( qId );

				if(  qId.substring(0,2).equals("VH") ){

					Question question = questions.get( qId );

					if( response.timeTaken >=  question.fifthPercentile ){
						response.result = 1;
						Integer c = qType.get( response.type );
						if( c == null ){
							c = 0;
						}
						c++;
						qType.put( response.type, c );
						count++;
					}

					if( response.actions.size() >=  question.fifthPercentileAction ){
						response.actionresult = 1;
					}
				}

				responses.put(qId, response );
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

		calculateApplicant("3.0");
		calculateApplicant("2.0");
		calculateApplicant("1.0");
	}

	void print(){
		Block block = blocks.get("A");
		block.print();
	}
}

class Training{

	Data data;

	Training(){
		data = new Data();
	}

	void readTrainingData(String filename, boolean header){
		data.readTraingData(filename, true);
	}

	void readTrainingTarget(String filename, boolean header){
		data.readTrainingTarget(filename, true);
	}
	
	void createData(){

		data.create();	
		data.calculate5thPecentile();
	}

	void printData(){

		data.print();

		data.printTrainingHeader();
		data.printTrainingData("0.3");
		//data.printTrainingData("0.2");
		//data.printTrainingData("0.1");
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
	
		Training training = new Training();
		training.readTrainingData( filename, true );
		training.readTrainingTarget( tarfilename, true );
		training.createData();
		training.printData();
	}
}
