import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import ttest_rel

def read_variable_length_csv(file_path):
    data = []
    with open(file_path, 'r') as file:
        for line in file:
            values = line.strip().split(',')
            try:
                float_values = [float(value) for value in values if value]
                data.append(float_values)
            except ValueError:
                continue  
    return data

def collect_absolute_differences(data1, data2):
    differences = []
    for values1, values2 in zip(data1, data2):
        common_length = min(len(values1), len(values2))
        differences.extend([abs(v1 - v2) for v1, v2 in zip(values1[:common_length], values2[:common_length])])
    return differences

def perform_paired_t_test(data1, data2):
    min_length = min(len(data1), len(data2))
    paired_data1 = [np.mean(d) for d in data1[:min_length]]
    paired_data2 = [np.mean(d) for d in data2[:min_length]]
    return ttest_rel(paired_data1, paired_data2)


gaus_data = read_variable_length_csv('src/gaus.csv')
gaus_choice_data = read_variable_length_csv('src/gausChoice.csv')


for i in range(min(len(gaus_data), len(gaus_choice_data))):
    t_test_result = perform_paired_t_test(gaus_data[i], gaus_choice_data[i])
    
    all_differences = collect_absolute_differences([gaus_data[i]], [gaus_choice_data[i]])
    
    plt.figure(figsize=(10, 5))
    plt.plot(all_differences, marker='o', linestyle='', markersize=5)
    plt.title(f'Difference between A1 and A2')
    plt.xlabel('X(n)')
    plt.ylabel('Absolute Difference')
    plt.grid(True)
    plt.savefig(f'h1_plot_{i}.jpg')  
    plt.close()  


