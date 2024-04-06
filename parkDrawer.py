import os
import networkx as nx
import matplotlib.pyplot as plt


def create_graph_from_file(file_path):
    G = nx.Graph()
    
    with open(file_path, 'r') as file:
        for line in file:
            edge_info = line.strip().split()
            if len(edge_info) == 3:  # Ensures proper format per line
                node1, node2, edge_length = edge_info[0], edge_info[1], int(edge_info[2])
                G.add_edge(node1, node2, length=edge_length)  # Automatically adds nodes if they don't exist
    
    return G

def draw_graph(graph):
    pos = nx.shell_layout(graph, nlist=[list(graph.nodes)], scale=2)  
    edge_labels = nx.get_edge_attributes(graph, 'length')
    nx.draw(graph, pos=pos, with_labels=True, node_color='skyblue', edge_color='gray')
    nx.draw_networkx_edge_labels(graph, pos, edge_labels=edge_labels)
    plt.title("User-Defined Graph with Edge Lengths")
    plt.show()

if __name__ == "__main__":
    file_path = "Downloads/test/a.txt"  # Make sure this path is correct
    graph = create_graph_from_file(file_path)
    draw_graph(graph)
