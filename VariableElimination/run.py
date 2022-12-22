"""
@Author: Joris van Vugt, Moira Berens, Leonieke van den Bulk

Entry point for the creation of the variable elimination algorithm in Python 3.
Code to read in Bayesian Networks has been provided. We assume you have installed the pandas package.

"""
import pandas as pd
from read_bayesnet import BayesNet
from variable_elim import VariableElimination

if __name__ == '__main__':
    # The class BayesNet represents a Bayesian network from a .bif file in several variables
    net = BayesNet('earthquake.bif') # Format and other networks can be found on http://www.bnlearn.com/bnrepository/
    
    # These are the variables read from the network that should be used for variable elimination
    print("Nodes:")
    print(net.nodes)
    print("Values:")
    print(net.values)
    print("Parents:")
    print(net.parents)
    print("Probabilities:")
    print(pd.merge(net.probabilities['Alarm'], net.probabilities['JohnCalls'], on='Alarm'))

    # Make your variable elimination code in the seperate file: 'variable_elim'. 
    # You use this file as follows:
    df = pd.merge(net.probabilities['Alarm'], net.probabilities['MaryCalls'], on='Alarm')

    df['prob'] = df['prob_x'] * df['prob_y']
    df.pop('prob_x')
    df.pop('prob_y')
    print(df)
    
    ve = VariableElimination(net)

    # Set the node to be queried as follows:
    query = 'Alarm'

    # The evidence is represented in the following way (can also be empty when there is no evidence): 
    evidence = {'Burglary': 'True'}

    # Determine your elimination ordering before you call the run function. The elimination ordering   
    # is either specified by a list or a heuristic function that determines the elimination ordering
    # given the network. Experimentation with different heuristics will earn bonus points. The elimination
    # ordering can for example be set as follows:
    elim_order = net.nodes

    # Call the variable elimination function for the queried node given the evidence and the elimination ordering as follows:   
    # ve.run(query, evidence, elim_order)
