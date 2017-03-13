/*
*  
*  Author  : Rashid A. Aljohani
*  Thursday, March 13, 2017
*
*/


import java.util.*;


public class Helper{
	
	final static int number_of_inputs_units = 2;
	final static int number_of_hidden_units = 3;

	public void init(List<Node> inputs_units, List<Node> hiddens_units, Node output_unit, Map<Node, Set<Weight>> connections){

		// MARK: initialized the units : inputs units
		for(int i=0; i < number_of_inputs_units; i += 1){
			inputs_units.add( new Node("input[" + (i+1) + "] "));
		}

		// MARK: initialized the units : hiddens units
		for(int i=0; i < number_of_hidden_units; i += 1){
			hiddens_units.add( new Node("hidden[" + (i+1) + "] "));
		}

		// MARK: set up connection weights : inputs units ~> hiddens units;
		for(Node hidden : hiddens_units){

			Set<Weight> set;
			if( !connections.containsKey(hidden) ){
				set = new LinkedHashSet<Weight>();

			}else{
				set = connections.get(hidden);
			}

			for(Node input : inputs_units){

				Weight weight = new Weight(input, random(), hidden);

				set.add(weight);
				connections.put(hidden, set);
			}
		}


		// MARK: set up connection weights : hiddens units ~>  output unit;
		Set<Weight> set = new LinkedHashSet<Weight>();
		connections.put(output_unit, set);
		for(Node hidden : hiddens_units){

			Weight weight = new Weight(hidden, random(), output_unit);
			set = connections.get(output_unit);
			set.add(weight);

			connections.put(output_unit, set);
		}


	}


	public void feedforward(Map<Node, Set<Weight>> connections){

		for(Node key : connections.keySet() ){

			// MARK : get only hidden units, then get the summation and set the actual value
			if(key.toString().charAt(0) == 'h'){ 

				double sum = 0.0;
				for(Weight weight : connections.get(key) ){

					sum += ( weight.get_source().get_input() * weight.get_weight() );
				}

				key.set_actual(sum);


			}else{ // this is the output unit, get the summation and set the actual value
	
				double sum = 0.0;
				for(Weight weight : connections.get(key) ){

					sum += ( weight.get_source().get_actual() * weight.get_weight() );
				}

				key.set_actual(sum);					
			}
		
		}
	}


	public void backpropagation(Map<Node, Set<Weight>> connections, Node output_unit, double lc){

		// MARK: store a copy of the old weights: hiddens ~> outputs
		Set<Weight> old_weights = new HashSet<>(connections.get(output_unit));

		// MARK: find delta: output
		double delta = (output_unit.get_target() - output_unit.get_actual()) * output_unit.get_actual() * (1 - output_unit.get_actual());
		output_unit.set_delta(delta);


		// MARK: update connections weights: hiddens ~> outputs
		for(Node output : connections.keySet() ){

			if(output.toString().charAt(0) == 'o'){

				for(Weight weight : connections.get(output) ){

					double hidden_delta = delta * weight.get_weight() *
										 weight.get_source().get_actual() * (1 - weight.get_source().get_actual()); 

					// MARK: set delta value of hidden units
					weight.get_source().set_delta(hidden_delta);

					// MARK: update connection weights : hiddens ~> outputs
					weight.update_weight_outputs(output_unit.get_delta(), lc);
				}
			}
		}


		// MARK: update connections weights: inputs ~> hiddens
		for(Node hidden : connections.keySet() ){

			if(hidden.toString().charAt(0) == 'h'){

				for(Weight weight : connections.get(hidden) ){
					weight.update_weight_hiddens(lc);
				}
			}
		}

	}



	static double random(){
		return Math.random();
	}
}
