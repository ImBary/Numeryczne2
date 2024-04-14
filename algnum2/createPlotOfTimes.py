import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

czasy_data = pd.read_csv('src/czasy.csv',delimiter=', ',engine='python')

plt.plot(range(1, len(czasy_data['A1']) + 1), czasy_data['A1'],marker='o',color=(1,0,0),label='A1')
plt.plot(range(1, len(czasy_data['A2']) + 1), czasy_data['A2'],marker='o',color=(0,0,1),label='A2')
plt.plot(range(1, len(czasy_data['A3']) + 1), czasy_data['A3'],marker='o',color=(0,1,0),label='A3')

plt.xlabel('n')
plt.ylabel('times')
plt.title('czasy')
plt.legend()
plt.xticks(range(1, len(czasy_data['A1']) + 1))
plt.savefig('czasy.jpg')
plt.show()