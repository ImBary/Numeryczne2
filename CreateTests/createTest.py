import random

def generate_test_case(n, m, max_distance=10, max_walkers=10, max_trash_bins=0):
    test_case = f"{n} {m}\n"

    for _ in range(m):
        i = random.randint(1, n)
        j = random.randint(1, n)
        while j == i:
            j = random.randint(1, n)
        dij = random.randint(2, max_distance)
        test_case += f"{i} {j} {dij}\n"

    osk_count = max(1, min(n, random.randint(1, 3)))
    exits_count = max(1, min(n, random.randint(1, 3)))
    walkers_count = random.randint(1, max_walkers)

    osk_positions = random.sample(range(1, n+1), osk_count)
    exits_positions = random.sample(range(1, n+1), exits_count)

    osk_positions = [pos for pos in osk_positions if pos not in exits_positions]
    exits_positions = [pos for pos in exits_positions if pos not in osk_positions]

    test_case += f"{len(osk_positions)} "
    for pos in osk_positions:
        test_case += f"{pos} "
    test_case += "\n"

    test_case += f"{len(exits_positions)} "
    for pos in exits_positions:
        test_case += f"{pos} "
    test_case += "\n"

    test_case += f"{walkers_count}\n"

    return test_case

def save_to_file(test_case, filename):
    with open(filename, 'w') as file:
        file.write(test_case)

n = random.randint(1, 10)
m = random.randint(1, 10)
test_case = generate_test_case(n, m)
filename = "test_case.txt"
save_to_file(test_case, filename)
print(f"Test case saved to {filename}")

print(test_case)
