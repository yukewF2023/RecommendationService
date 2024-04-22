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
                matrix[user_idx, product_idx] += interaction['number_of_view']

    return matrix, product_index


def recommend_products(matrix, user_index, product_index, all_products):
    similarities = cosine_similarity(matrix)
    user_similarity = similarities[user_index]
    similar_users = np.argsort(-user_similarity)
    product_scores = np.dot(user_similarity[similar_users], matrix[similar_users])

    recommended_scores = sorted(((score, prod) for prod, score in enumerate(product_scores) if prod not in matrix[user_index].nonzero()[0]), reverse=True, key=lambda x: x[0])
    recommended_product_ids = [list(product_index.keys())[prod] for _, prod in recommended_scores]

    non_reviewed_product_ids = [prod['product_id'] for prod in all_products if prod['product_id'] not in recommended_product_ids]
    already_viewed = list(set(matrix[user_index].nonzero()[0]) - set(recommended_product_ids))
    already_viewed = [list(product_index.keys())[idx] for idx in already_viewed]

    final_recommendations = recommended_product_ids + non_reviewed_product_ids + already_viewed
    final_recommendations = list(dict.fromkeys(final_recommendations))  # Remove duplicates while preserving order

    return final_recommendations

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
