import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

gaus = [0.5999999999999999, 0.5999999999999998, 0.19999999999999957, 0.7692307692307679, 0.8301886792452826, 0.5441382707580487, 0.6678166584524398, 0.7499999999999998,0.5493600066008839]
gausSiedel = [0.5999999999999999, 0.5999999999999998, 0.19999999999999957, 0.7692307692307679, 0.8301886792452826, 0.5441344558544479, 0.6678166584524399, 0.7499999999999999,0.5493600066008839]
gausChoice = [0.5999999999999999, 0.5999999999999998, 0.19999999999999957, 0.7692307692307679, 0.8301886792452826, 0.5441382707580487, 0.6678166584524399, 0.7499999999999999,0.5493600066008839]
monte = [0.5932, 0.6013, 0.1996, 0.7643, 0.833, 0.542, 0.6624, 0.7453,0.5554]

# Calculate differences
diff_monte_gaus = np.array(monte) - np.array(gaus)
diff_monte_gausSiedel = np.array(monte) - np.array(gausSiedel)
diff_monte_gausChoice = np.array(monte) - np.array(gausChoice)
diff_gaus_gausSiedel = np.array(gaus) - np.array(gausSiedel)
diff_gaus_gausChoice = np.array(gaus) - np.array(gausChoice)
diff_gausChoice_gausSiedel = np.array(gausChoice) - np.array(gausSiedel)

# Plotting

plt.plot(diff_monte_gausSiedel, marker='o')
plt.title('Monte - GausSiedel')


# Saving plots as PNG
plt.savefig('comparison_plots.png')
#plt.show()