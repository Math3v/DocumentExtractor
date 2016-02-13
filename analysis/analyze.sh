grep -E 'Count: [1-9]{1}[0-9]*' -B 1 out.txt | grep -E '^[0-9] ?\.' | sort | uniq
