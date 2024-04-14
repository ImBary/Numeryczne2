import os
import networkx as nx
import matplotlib.pyplot as plt


num = os.environ.get("num")

def create_graph_from_file(file_path):
    G = nx.Graph()
    linec = 0
    osk = []
    exits = []
    wanderer = 0
    with open(file_path, 'r') as file:
        alleys = 0
        for line in file:
            linec = linec + 1
            info = line.strip().split()
            if (linec == 1):
                alleys = int(info[1])
            elif linec <= alleys + 1:
                node1, node2, edge_length = info[0], info[1], int(info[2])
                G.add_edge(node1, node2, length=edge_length)
            elif linec == alleys + 2:
                oskNum = int(info[0])
                osk = [0]*oskNum
                for i in range(oskNum):
                    osk[i] = info[i+1]
            elif linec == alleys + 3:
                exitNum = int(info[0])
                exits = [0]*exitNum
                for i in range(exitNum):
                    exits[i] = info[i+1]
            else:
                wanderer = info[0]
    draw_graph(G, osk, exits, wanderer)

def draw_graph(graph, osk, exits, wanderer):
    pos = nx.spring_layout(graph)
    edge_labels = nx.get_edge_attributes(graph, 'length')
    nodes = list(graph.nodes)

    osk_nodes = [node for node in nodes if node in osk]
    exit_nodes = [node for node in nodes if node in exits]
    node_colors = ['red' if node in osk_nodes else 'skyblue' if node in exit_nodes else 'purple' if node == wanderer else 'green' for node in nodes]

    nx.draw(graph, pos=pos, with_labels=True, node_color=node_colors, edge_color='gray')
    nx.draw_networkx_edges(graph, pos, edgelist=graph.edges(), edge_color='gray')
    nx.draw_networkx_labels(graph, pos)
    nx.draw_networkx_edge_labels(graph, pos, edge_labels=edge_labels)
    plt.savefig(f"Graphs/{num}.jpg")

file_path = "src/test_case.txt"
create_graph_from_file(file_path)
