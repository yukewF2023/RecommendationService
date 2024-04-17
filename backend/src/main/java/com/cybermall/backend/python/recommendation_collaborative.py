import sys
import json
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
import random

def build_interaction_matrix(view_histories, products):
    # Map product IDs to indices for matrix operations
    product_index = {product['product_id']: idx for idx, product in enumerate(products)}
    num_users = len(view_histories)
    num_products = len(products)
    matrix = np.zeros((num_users, num_products))

    # Ensure view_histories is a list of list of dictionaries
    if not all(isinstance(histories, list) for histories in view_histories):
        view_histories = [view_histories]  # Encapsulate in list if it's not a list of lists

    for user_idx, histories in enumerate(view_histories):
        for interaction in histories:  # Directly access each interaction dictionary
            product_idx = product_index.get(interaction['product_id'])
            if product_idx is not None:
                matrix[user_idx, product_idx] = interaction['number_of_view']

    return matrix, product_index


def recommend_products(matrix, user_index, product_index, all_products):
    # Compute cosine similarity between users
    similarities = cosine_similarity(matrix)
    user_similarity = similarities[user_index]

    # Sort other users by similarity to the target user
    similar_users = np.argsort(-user_similarity)

    # Aggregate product scores from similar users
    product_scores = np.dot(user_similarity[similar_users], matrix[similar_users])

    # Get product IDs sorted by the computed scores
    recommended_product_ids = [product for product, idx in sorted(product_index.items(), key=lambda x: -product_scores[x[1]])]

    # Filter out already viewed products by the user
    viewed_products = set(matrix[user_index].nonzero()[0])
    recommended_product_ids = [pid for pid in recommended_product_ids if product_index[pid] not in viewed_products]

    # Include non-reviewed products sorted by popularity (number of views in this example)
    non_reviewed_products = sorted((prod for prod in all_products if product_index[prod['product_id']] not in viewed_products),
                                   key=lambda x: -x['number_of_view'])
    recommended_product_ids += [prod['product_id'] for prod in non_reviewed_products]

    # Add already viewed items randomly at the end
    already_viewed = [prod['product_id'] for idx, prod in enumerate(all_products) if idx in viewed_products]
    random.shuffle(already_viewed)
    recommended_product_ids += already_viewed

    return recommended_product_ids

if __name__ == "__main__":
    try:
        data = sys.stdin.read()
        input_data = json.loads(data)
        user_view_histories = input_data['user_view_histories']  # Single list of dictionaries
        all_view_histories = input_data['all_view_histories']  # List of lists of dictionaries
        all_products = input_data['all_products']

        # Assume current user is the first in the list for simplicity
        matrix, product_index = build_interaction_matrix(all_view_histories, all_products)
        user_index = 0  # current user index, adjust as necessary
        recommended_product_ids = recommend_products(matrix, user_index, product_index, all_products)

        print(json.dumps(recommended_product_ids))
    except json.JSONDecodeError as e:
        print(f"Error parsing JSON input: {e}", file=sys.stderr)
        sys.exit(1)
