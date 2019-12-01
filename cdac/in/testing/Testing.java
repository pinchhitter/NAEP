package cdac.in.testing;

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

	double fifthPercentile;
	double fifthPercentileAction;

	Set<Double> alltime;
	Set<Integer> allAction;

	Map<Double, Double> percentileTime;
	Map<Integer, Double> percentileAction;

	Question(String id, String type){

		this.id = id;
		this.type = type;

		this.totalTime = 0;
		this.totalAction = 0;

		this.fifthPercentile = 0.0d;
		this.fifthPercentileAction = 0.0d;

		this.alltime = new TreeSet<Double>();
		this.allAction = new TreeSet<Integer>();

		this.percentileTime = new TreeMap<Double, Double>();
		this.percentileAction = new TreeMap<Integer, Double>();
	}
}

class Response{

	String questionId;
	String type;
	String enterTime;
	String exitTime;
	double timeTaken;

	Double responsePercentile;
	Double actionPercentile;

	int seqN;
	int result;

	List<String> actions;

	Response(String id, String type){

		this.questionId = questionId;
		this.type = type;
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
	Map<String, String> blockResult;
	Map<String, Integer> qType;
	Map<String, Double>  qTypeCount;
	Map<String, Double>  qTypePercentile;

	int BlockRev;
        int EOSTimeLeft;
    	int SecTimeOut;
        int HELPMAT8;

	Applicant(String id){

		this.id  = id;
		blockResult = new TreeMap<String, String>();
		blockResult.put("A", "0.0");
		blockResult.put("B", "0.0");

		BlockRev = 0;
		EOSTimeLeft = 0;
		SecTimeOut = 0;
		HELPMAT8 = 0;
		this.qType = new TreeMap<String,Integer>();
		this.qTypePercentile = new TreeMap<String, Double>();
		this.qTypeCount = new TreeMap<String,Double>();
		this.responses = new LinkedHashMap<String, Response>();
	}
}

class QtypePercentile{

	String type;
	TreeSet<Double> allTimeTaken;
	Map<Double, Double> percentiles;

	QtypePercentile(String type){
		this.type = type;
		this.allTimeTaken = new TreeSet<Double>();
		this.percentiles = new TreeMap<Double, Double>();
	}
	
	void calculatePercentile(){
		
		ArrayList<Double> alltime = new ArrayList<>( allTimeTaken );
		Collections.sort( alltime );

		for(int i = 0; i < alltime.size() ; i++){
			double percentile = ( Math.round( (double) i / (double) alltime.size()  * (double) 100 ) ) / (double) 100;
			percentiles.put( alltime.get(i), percentile ); 
		}
	}
}

class Data{

	Map<String, Question> questions30;
	Map<String, Question> questions20;
	Map<String, Question> questions10;

	Map<String, Applicant> applicants30;
	Map<String, Applicant> applicants20;
	Map<String, Applicant> applicants10;
	 	
	Map<String, QtypePercentile> qTypePercentile30;
	Map<String, QtypePercentile> qTypePercentile20;
	Map<String, QtypePercentile> qTypePercentile10;

	Set<String> qTypes;


	Data(){

		this.applicants30  =  new TreeMap<String, Applicant>();
		this.applicants20  =  new TreeMap<String, Applicant>();
		this.applicants10  =  new TreeMap<String, Applicant>();

		this.questions30  =  new TreeMap<String, Question>();
		this.questions20  =  new TreeMap<String, Question>();
		this.questions10  =  new TreeMap<String, Question>();

		qTypePercentile30 =  new TreeMap<String, QtypePercentile>();
		qTypePercentile20 =  new TreeMap<String, QtypePercentile>();
		qTypePercentile10 =  new TreeMap<String, QtypePercentile>();
	
		this.qTypes = new TreeSet<String>();
	}

	void printTestingHeader(){

		System.out.println("@relation 'NAEP TRAINING DATA SET'");
		System.out.println("@attribute time {0.3,0.2,0.1}");

		for(String qid: questions30.keySet() ){

			//System.out.println("@attribute "+qid+"-Time numeric");
			System.out.println("@attribute "+qid+"-TimePercentile numeric");
			System.out.println("@attribute "+qid+"-ActionPercentile numeric");
			//System.out.println("@attribute "+qid+"ETU {0,1}");
		}

		for(String qid: qTypes ){
			System.out.println("@attribute "+qid+"-Percentile numeric");
			//System.out.println("@attribute "+qid+"-Count numeric");
		}
		
		System.out.println("@attribute BlockRev numeric ");
		System.out.println("@attribute EOSTimeLft numeric");
		System.out.println("@attribute SecTimeOut numeric");
		System.out.println("@attribute HELPMAT8 numeric");
		//System.out.println("@attribute BlockA numeric");
		System.out.println("@attribute class {1.0, 0.0}");
		System.out.println("@data");
	}

	void printTestingData(Map<String,Applicant> applicants, String time){

		for(String appId: applicants.keySet() ){

			Applicant applicant = applicants.get( appId );

			System.out.print(time);

			for( String qId: questions30.keySet() ){

				Question q = questions30.get( qId );
				Response res = applicant.responses.get( qId );

				if( res != null ){
					//System.out.print(", "+res.timeTaken+", "+res.responsePercentile);
					//System.out.print(","+res.timeTaken+","+res.responsePercentile+","+res.actionPercentile+", "+res.result);
					System.out.print(","+res.responsePercentile+","+res.actionPercentile);
					//System.out.print(", "+res.timeTaken+ ", "+res.responsePercentile+", "+res.result);
					//System.out.print(", "+res.responsePercentile);
					//System.out.print(", "+res.responsePercentile+", "+res.actionPercentile);
					//System.out.print(", "+res.timeTaken+", "+res.responsePercentile);
					//System.out.print(", "+res.timeTaken+ ", "+res.result);
				}else{
					//System.out.print(", 0.0, 0.0, 0.0");
					//System.out.print(", 0.0, 0.0, 0.0, 0");
					System.out.print(",0.0, 0.0");
					//System.out.print(", 0.0");
					//System.out.print(", 0.0, 0");
				}
			}

			for(String qId: qTypes ){
			
				Double percentile =  applicant.qTypePercentile.get( qId );
				if( percentile == null){
					percentile = 0.0d;
				}
				System.out.print(", "+percentile);

				/*
				Integer count = 0;
				count = applicant.qType.get( qId );
				if( count == null)
					System.out.print(", 0");
				else
					System.out.print(", "+(double)count / (double) 10);

				*/
			}
			
			System.out.print(", "+applicant.BlockRev+", "+applicant.EOSTimeLeft+", "+applicant.SecTimeOut+", "+applicant.HELPMAT8);
			//System.out.print(", "+applicant.BlockRev+", "+applicant.EOSTimeLeft+", "+applicant.SecTimeOut);
			//System.out.print(", "+applicant.BlockRev+", "+applicant.SecTimeOut);
			//System.out.print(", "+applicant.BlockRev);
			//System.out.print(", "+applicant.blockResult.get("A"));
			System.out.println(", ?");
		}
	}

	void readTestingData( String filename, boolean header, String time){

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

				if( time.equals("0.3"))
					applicant = applicants30.get( applicantId );
				if( time.equals("0.2"))
					applicant = applicants20.get( applicantId );
				if( time.equals("0.1"))
					applicant = applicants10.get( applicantId );

				if( applicant == null){
					applicant = new Applicant( applicantId );
				}

				Response response = applicant.responses.get( qId );

				if( response == null){
					response = new Response( qId, qType );
				}

				if( action.equals("Enter Item") ){
					response.enterTime = eventTime;
				}else if ( action.equals("Exit Item") ){
					response.exitTime = eventTime;
				}else{
					if( response.enterTime == null ){
						response.enterTime = eventTime;
					}else if ( response.exitTime == null && response.enterTime != null ){
						response.exitTime = eventTime;
					}	

					response.actions.add( info );
				}
				applicant.responses.put( qId, response );

				if( time.equals("0.3"))
					applicants30.put( applicantId, applicant );
				if( time.equals("0.2"))
					applicants20.put( applicantId, applicant );
				if( time.equals("0.1"))
					applicants10.put( applicantId, applicant );
			} 

			System.err.println("Total Data Read: "+count+" applicant Count: "+applicants30.size());
			System.err.println("Total Data Read: "+count+" applicant Count: "+applicants20.size());
			System.err.println("Total Data Read: "+count+" applicant Count: "+applicants10.size());

		}catch(Exception e){

			System.out.println("Line: '"+line.trim()+" '");
			e.printStackTrace();
		}
	}

	void create(Map<String, Applicant> applicants, Map<String, Question> questions){

		for(String appId: applicants.keySet() ){

			Applicant applicant = applicants.get( appId );

			double timeTaken = 0;

			for(String repId: applicant.responses.keySet() ){

				Response response = applicant.responses.get( repId );

				try{
					Timestamp et = Timestamp.valueOf( response.enterTime);
					Timestamp xt = Timestamp.valueOf( response.exitTime );
					long milliseconds = xt.getTime() - et.getTime();
					double seconds = (double) milliseconds / 1000;
					response.timeTaken = (float) ( seconds % 3600 ) / (double) 60 ;

				}catch(Exception e){
					response.timeTaken = 0.0d;
				}	

				timeTaken += response.timeTaken;

				if(  repId.substring(0,2).equals("VH") ){

					Question question = questions.get( repId );
					if( question == null ){
						question =  new Question( repId, response.type );
					}

					question.totalTime += response.timeTaken;
					question.totalAction += response.actions.size();
					question.alltime.add( response.timeTaken );
					question.allAction.add( response.actions.size() );
					questions.put( repId, question );

					Double timetaken = applicant.qTypeCount.get( question.type );
					if( timetaken == null ){
						timetaken = 0.0d;
					}
					timetaken += response.timeTaken;
					applicant.qTypeCount.put( question.type, timetaken );

				}else if ( repId.equals("BlockRev")  ){
					applicant.BlockRev = 1;

				}else if ( repId.equals("EOSTimeLft") ){
					applicant.EOSTimeLeft= 1;

				}else if( repId.equals("SecTimeOut")){
					applicant.SecTimeOut = 1;
				}else if( repId.equals("HELPMAT8")  ){
					applicant.HELPMAT8 = 1;
				}
				applicant.responses.put( repId, response );
			}
			applicants.put( appId, applicant );
		}
	}

	void calculateApplicant(Map<String, Applicant> applicants, Map<String, Question> questions, Map<String, QtypePercentile> qtypePercentile){

		for(String id: applicants.keySet() ){

			Applicant applicant = applicants.get( id ); 
			Map<String,Response> responses = applicant.responses;
			Map<String,Integer> qType = applicant.qType;
			int count = 0;
			for(String qId: responses.keySet() ){
				Response response = responses.get( qId );
				if(  qId.substring(0,2).equals("VH") ){
					Question question = questions.get( qId );
					if( response.timeTaken >=  question.fifthPercentile ){
						response.result = 1;
						count++;
					}
					Integer c = qType.get( response.type );
					if( c == null ){
					     c = 0;
					}
					c++;
					qType.put( response.type, c );
					qTypes.add( response.type );
					
					response.responsePercentile = question.percentileTime.get( response.timeTaken );
					response.actionPercentile = question.percentileAction.get( response.actions.size() );

					if( response.responsePercentile == null )
						response.responsePercentile = 0.0d;
					if( response.actionPercentile == null )
						response.actionPercentile = 0.0d;
				}
				responses.put( qId, response );
			}

			for(String type: applicant.qTypeCount.keySet() ){

				QtypePercentile qptc = qtypePercentile.get( type );
				applicant.qTypePercentile.put( type, qptc.percentiles.get( applicant.qTypeCount.get( type  ) ) );
			}

			if( count == questions.size() ){
				applicant.blockResult.put("A", "1.0");
			}
			applicants.put( id, applicant );
		}
	}

	void calulatePercentile(Map<String, Question> questions, Map<String, Applicant> applicants, Map<String, QtypePercentile> qtypePercentile){


		for(String id: applicants.keySet() ){
			Applicant applicant = applicants.get( id );
			for(String type: applicant.qTypeCount.keySet() ){
				QtypePercentile qptc = qtypePercentile.get( type );
				if( qptc == null){
					qptc = new QtypePercentile( type );
				}
				qptc.allTimeTaken.add( applicant.qTypeCount.get( type ) );
				qtypePercentile.put( type, qptc );
			}
		}	

		for(String type: qtypePercentile.keySet() ){
			qtypePercentile.get(type).calculatePercentile();
		}	

		for(String id: questions.keySet()){

			Question question = questions.get( id );

			ArrayList<Double> alltime = new ArrayList<>( question.alltime  );
			Collections.sort( alltime );
			long index =  Math.round( ( (double)5 / (double)100 ) * (double) alltime.size() ); 
			question.fifthPercentile = alltime.get( (int)index ); 

			for(int i = 0; i < alltime.size() ; i++){
				double percentile = ( Math.round( (double) i / (double) alltime.size()  * (double) 100 ) ) / (double) 100;
				question.percentileTime.put( alltime.get(i), percentile ); 
			}

			ArrayList<Integer> all = new ArrayList<>( question.allAction );
			Collections.sort( all );
			for(int i = 0; i < all.size() ; i++){
				double percentile = ( Math.round( (double) i / (double) all.size()  * (double) 100 ) ) / (double) 100;
				question.percentileAction.put( all.get(i), percentile ); 
			}
			questions.put( id, question );
		}
	}


	void calculatePecentile(){

		calulatePercentile( questions30 , applicants30, qTypePercentile30 ); 
		calulatePercentile( questions20 , applicants20, qTypePercentile20 ); 
		calulatePercentile( questions10 , applicants10, qTypePercentile10 ); 

		calculateApplicant( applicants30, questions30, qTypePercentile30 );
		calculateApplicant( applicants20, questions20, qTypePercentile20 );
		calculateApplicant( applicants10, questions10, qTypePercentile10 );
	}

}

class Testing{

	Data data;

	Testing(){
		data = new Data();
	}

	void readTestingData(String filename, boolean header, String time){
		data.readTestingData(filename, header, time);
	}

	void createData(){

		data.create( data.applicants30, data.questions30 );	
		data.create( data.applicants20, data.questions20 );	
		data.create( data.applicants10, data.questions10 );	

		data.calculatePecentile();
	}

	void printData(){

		data.printTestingHeader();

		data.printTestingData( data.applicants30, "0.3" );
		data.printTestingData( data.applicants20, "0.2" );
		data.printTestingData( data.applicants10, "0.1" );
	}	
	
	
	public static void main(String[] args){

		Testing testing = new Testing();
		testing.readTestingData( "../data/data_a_hidden_30.csv", true, "0.3" );
		testing.readTestingData( "../data/data_a_hidden_20.csv", true, "0.2" );
		testing.readTestingData( "../data/data_a_hidden_10.csv", true, "0.1" );

		testing.createData();
		testing.printData();
	}
}
