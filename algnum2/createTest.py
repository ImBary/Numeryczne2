import random
import os

# Generate a random file name for the source file
source_file_name = f"/home/bary/kuszi2/Numeryczne2/algnum2/test_cases/{random.randint(1, 3)}.txt"
destination_file_name = "src/test_case.txt"  # Destination file name


# Now, copy the contents from the source file to the destination file
def copy_contents(source_file_path, destination_file_path):
    with open(source_file_path, 'r') as source_file:
        with open(destination_file_path, 'w') as destination_file:
            destination_file.write(source_file.read())

# Copy the contents from the source to the destination
copy_contents(source_file_name, destination_file_name)


