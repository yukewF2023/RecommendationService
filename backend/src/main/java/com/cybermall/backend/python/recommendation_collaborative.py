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
        # data = '{"user_id": 1, "user_view_histories": [{"user_id": 1, "product_id": 1, "number_of_view": 2, "price": 10.0, "category": "fruit"},{"user_id": 1, "product_id": 2, "number_of_view": 2, "price": 20.0, "category": "fruit"},{"user_id": 1, "product_id": 3, "number_of_view": 2, "price": 15.0, "category": "book"},{"user_id": 1, "product_id": 4, "number_of_view": 2, "price": 25.0, "category": "book"}], "all_view_histories": [{"user_id": 1, "product_id": 1, "number_of_view": 2, "price": 10.0, "category": "fruit"},{"user_id": 1, "product_id": 2, "number_of_view": 2, "price": 20.0, "category": "fruit"},{"user_id": 1, "product_id": 3, "number_of_view": 2, "price": 15.0, "category": "book"},{"user_id": 1, "product_id": 4, "number_of_view": 2, "price": 25.0, "category": "book"}], "all_products": [{"product_id": 1, "number_of_view": 2, "price": 10.0, "category": "fruit"},{"product_id": 2, "number_of_view": 2, "price": 20.0, "category": "fruit"},{"product_id": 3, "number_of_view": 2, "price": 15.0, "category": "book"},{"product_id": 4, "number_of_view": 2, "price": 25.0, "category": "book"},{"product_id": 5, "number_of_view": 1, "price": 30.0, "category": "book"},{"product_id": 6, "number_of_view": 1, "price": 35.0, "category": "book"},{"product_id": 7, "number_of_view": 1, "price": 40.0, "category": "book"},{"product_id": 8, "number_of_view": 1, "price": 5.0, "category": "vegetable"},{"product_id": 9, "number_of_view": 1, "price": 7.0, "category": "vegetable"},{"product_id": 10, "number_of_view": 1, "price": 8.0, "category": "vegetable"},{"product_id": 11, "number_of_view": 1, "price": 6.0, "category": "vegetable"},{"product_id": 12, "number_of_view": 1, "price": 9.0, "category": "vegetable"},{"product_id": 13, "number_of_view": 1, "price": 3.0, "category": "dairy"},{"product_id": 14, "number_of_view": 1, "price": 4.0, "category": "dairy"},{"product_id": 15, "number_of_view": 1, "price": 2.0, "category": "dairy"},{"product_id": 16, "number_of_view": 1, "price": 5.0, "category": "dairy"},{"product_id": 17, "number_of_view": 1, "price": 6.0, "category": "dairy"},{"product_id": 18, "number_of_view": 1, "price": 12.0, "category": "meat"},{"product_id": 19, "number_of_view": 1, "price": 15.0, "category": "meat"},{"product_id": 20, "number_of_view": 1, "price": 18.0, "category": "meat"},{"product_id": 21, "number_of_view": 1, "price": 20.0, "category": "meat"},{"product_id": 22, "number_of_view": 1, "price": 22.0, "category": "meat"},{"product_id": 23, "number_of_view": 1, "price": 10.0, "category": "kitchen"},{"product_id": 24, "number_of_view": 1, "price": 20.0, "category": "kitchen"},{"product_id": 25, "number_of_view": 1, "price": 30.0, "category": "kitchen"},{"product_id": 26, "number_of_view": 1, "price": 40.0, "category": "electronics"},{"product_id": 27, "number_of_view": 1, "price": 50.0, "category": "electronics"},{"product_id": 28, "number_of_view": 1, "price": 60.0, "category": "electronics"},{"product_id": 29, "number_of_view": 1, "price": 70.0, "category": "clothing"},{"product_id": 30, "number_of_view": 1, "price": 80.0, "category": "clothing"},{"product_id": 31, "number_of_view": 1, "price": 90.0, "category": "clothing"},{"product_id": 32, "number_of_view": 1, "price": 100.0, "category": "clothing"}]}'
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
